package com.animewebsite.system.model.pk;

import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.Character;
import com.animewebsite.system.model.VoiceActor;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimeCharacterVoiceActorId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id")
    private Anime anime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "voice_actor_id")
    private VoiceActor voiceActor;

}
