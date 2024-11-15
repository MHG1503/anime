package com.animewebsite.system.service;

import com.animewebsite.security.common.utils.CurrentUserUtils;
import com.animewebsite.system.dto.req.ChangePasswordRequest;
import com.animewebsite.system.dto.req.UserRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final CurrentUserUtils currentUserUtils;

    public PaginatedResponse<User> getAllUser(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Page<User> users = userRepository.findAll(pageable);

        return new PaginatedResponse<>(
                users.getContent(),
                users.getTotalPages(),
                users.getNumber()+1,
                users.getTotalElements()
        );
    }

    public User currentUser(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Not found email: " + email));
    }

    public User updateUserInfo(Long id,UserRequest userRequest){
        User currentUser = currentUser(currentUserUtils.getCurrentUserName());

        if(!currentUser.getId().equals(id)){
            throw new RuntimeException("Ban khong the thay doi thong tin cua 1 nguoi dung khac!");
        }

        Image image = null;
        try {
            MultipartFile multipartFile = userRequest.getAvatar();
            if(multipartFile != null){
                image = currentUser.getImage();
                if(image != null && image.getPublicId() != null) { // truong hop user da co avatar
                    cloudinaryService.deleteImage(image.getPublicId());
                    Map<String,String> results = cloudinaryService.basicUploadFile(multipartFile);
                    image.setImageUrl(results.get("image"));
                    image.setPublicId(results.get("public_id"));

                }else{ // truong hop user chua co avatar!
                    Map<String,String> results = cloudinaryService.basicUploadFile(multipartFile);
                    image = Image
                            .builder()
                            .publicId(results.get("public_id"))
                            .imageUrl(results.get("image"))
                            .build();
                    currentUser.setImage(image);
                }
            }

            String usernameRequest = userRequest.getUsername();
            currentUser.setUsername(usernameRequest);

            return userRepository.save(currentUser);
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai!");
        }
    }

    public String updateUserPassword(ChangePasswordRequest request, Long id){
        User currentUser = currentUser(currentUserUtils.getCurrentUserName());

        if(!currentUser.getId().equals(id)){
            throw new RuntimeException("Ban khong the thay doi thong tin cua 1 nguoi dung khac!");
        }

        if(!passwordEncoder.matches(request.oldPassword(), currentUser.getPassword())){
            throw new RuntimeException("Mat khau khong khop voi mat khau cu");
        }

        String newPassword = request.newPassword();
        String confirmPassword = request.confirmPassword();
        if(!Objects.equals(newPassword,confirmPassword)){
            throw new RuntimeException("Mat khau khong hop le, vui long nhap lai mat khau");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        userRepository.updateUserPassword(newPassword,currentUser);

        return "Thay doi mat khau thanh cong!";
    }


}
