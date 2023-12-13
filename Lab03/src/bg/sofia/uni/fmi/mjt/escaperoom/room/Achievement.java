package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

public class Achievement {
    private Team team;
    private int escapeTime;

    public Achievement(Team team, int escapeTime){
        this.escapeTime = escapeTime;
        this.team = team;
    }

    public Team getTeam(){
        return team;
    }

    public int getEscapeTime(){
        return escapeTime;
    }
}
