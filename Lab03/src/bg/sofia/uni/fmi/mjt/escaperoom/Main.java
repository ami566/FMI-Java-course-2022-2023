package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.*;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;
import bg.sofia.uni.fmi.mjt.escaperoom.team.TeamMember;

import java.time.LocalDateTime;

public class Main {
    public static void main(String... args) throws RoomAlreadyExistsException, RoomNotFoundException, TeamNotFoundException {
        Theme theme = Theme.HORROR;
        Difficulty dif = Difficulty.HARD;
        EscapeRoom escapeRoom = new EscapeRoom("Curse", theme, dif, 500, 30, 60);

        Review rev = new Review(7, "Noce");

        TeamMember tim = new TeamMember("Tim", LocalDateTime.now());
        TeamMember tom = new TeamMember("Tom", LocalDateTime.now());
        TeamMember[] team1 = {tim, tom};

        Team first = Team.of("First", team1);


        Team sec = Team.of("Sec", team1);
        Team[] teams = {first, sec};

        EscapeRoomPlatform platform = new EscapeRoomPlatform(teams, 3);
        platform.addEscapeRoom(escapeRoom);

        Achievement a = new Achievement(first, 90);
        Achievement b = new Achievement(first, 60);

        platform.registerAchievement("Curse", first.getName(), 350);
        platform.registerAchievement("Curse", sec.getName(), 200);
        System.out.println(platform.getTopTeamByRating().getName());


        //Review rev
    }
}
