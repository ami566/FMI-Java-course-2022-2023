package bg.sofia.uni.fmi.mjt.escaperoom.team;

import java.time.LocalDateTime;

public record TeamMember(String name, LocalDateTime birthday) {
    public TeamMember{
        if (name == null){
            throw new IllegalArgumentException();
        }
    }
}
