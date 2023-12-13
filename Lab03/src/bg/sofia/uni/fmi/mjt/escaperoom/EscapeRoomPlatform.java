package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.PlatformCapacityExceededException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Achievement;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

import java.util.Arrays;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI, EscapeRoomPortalAPI {

    private Team[] teams;
    private final int maxCapacity;
    private int size = 0;
    private EscapeRoom[] rooms;

    private boolean isStringInvalid(String str){
        return str == null || str.equals("") || str.isEmpty();
    }

    public EscapeRoomPlatform(Team[] teams, int maxCapacity){
        this.teams = teams;
        this.maxCapacity = maxCapacity;
        rooms = new EscapeRoom[1];
    }

    private int checkIfRoomExists(String roomName){
        for (int i = 0; i < size; i++){
            if (rooms[i].CompareRoom(roomName)){
                return i;
            }
        }
        return -1;
    }

    private int checkIfTeamExists(String teamName){
        for (int i = 0; i < teams.length; i++){
            if (teams[i].CompareTeamName(teamName)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addEscapeRoom(EscapeRoom room) throws RoomAlreadyExistsException {
        if (room == null){
            throw new IllegalArgumentException("Room cannot be null");
        }

        if (size == maxCapacity){
            throw new PlatformCapacityExceededException("The Platform is full.");
        }

        if (checkIfRoomExists(room.getName().trim()) != -1){
            throw new RoomAlreadyExistsException("Room with the name " + room.getName() + " already exists.");
        }

        rooms[size++] = room;

    }

    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException {

        if (roomName == null || isStringInvalid(roomName.trim())){
            throw new IllegalArgumentException("The room name cannot be blank or empty");
        }

        int indexOfRoom = checkIfRoomExists(roomName);
        if (indexOfRoom == -1){
            throw new RoomNotFoundException("Could not find room " + roomName);
        }

        EscapeRoom[] newArr = new EscapeRoom[--size];
        for (int i = 0; i < indexOfRoom; i++){
            newArr[i] = rooms[i];
        }
        for (int i = indexOfRoom + 1; i < size + 1; i++){
            newArr[i - 1] = rooms[i];
        }
       rooms = newArr;
    }

    @Override
    public EscapeRoom[] getAllEscapeRooms() {
        return rooms;
    }

    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime) throws RoomNotFoundException, TeamNotFoundException {


        if (isStringInvalid(roomName.trim())) {
            throw new IllegalArgumentException("The room name cannot be blank or empty");
        }

        if (teamName == null || isStringInvalid(teamName.trim())) {
            throw new IllegalArgumentException("The team name cannot be blank or empty");
        }

        if (escapeTime <= 0){
            throw new IllegalArgumentException("The escape time cannot be 0 or negative");
        }
        int indexOfRoom = checkIfRoomExists(roomName);
        if (indexOfRoom == -1){
            throw new RoomNotFoundException("Could not find room " + roomName);
        }

        if(rooms[indexOfRoom].getMaxTimeToEscape() < escapeTime){
            throw new IllegalArgumentException("The escape time cannot exceed the maximal time to escape from the room");
        }

        int indexOfTeam = checkIfTeamExists(teamName);
        if (indexOfTeam == -1){
            throw new TeamNotFoundException("Could not find team " + teamName);
        }

        rooms[indexOfRoom].addAchievement(new Achievement(teams[indexOfTeam], escapeTime));

        int points = rooms[indexOfRoom].getDifficulty().getRank();

        if (rooms[indexOfRoom].getMaxTimeToEscape() * 0.5 >= escapeTime){
            points += 2;
        }
        else if (rooms[indexOfRoom].getMaxTimeToEscape() * 0.5 < escapeTime && rooms[indexOfRoom].getMaxTimeToEscape() * 0.75 >= escapeTime){
            points++;
        }
        teams[indexOfTeam].updateRating(points);

    }

    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException {
       if (isStringInvalid(roomName.trim())){
           throw new IllegalArgumentException();
       }

       int indexOfRoom = checkIfRoomExists(roomName);
       if (indexOfRoom == -1){
           throw new RoomNotFoundException("Could not find room " + roomName);
       }

       return rooms[indexOfRoom];
    }

    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException {

        if (isStringInvalid(roomName.trim())){
            throw new IllegalArgumentException("The room name cannot be blank or empty");
        }

        if (review == null){
            throw new IllegalArgumentException("Review cannot be null");
        }

        int indexOfRoom = checkIfRoomExists(roomName);
        if (indexOfRoom == -1){
            throw new RoomNotFoundException("Could not find room " + roomName);
        }

        rooms[indexOfRoom].addReview(review);

    }

    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException {
        if (isStringInvalid(roomName.trim())){
            throw new IllegalArgumentException("The room name cannot be blank or empty");
        }

        int indexOfRoom = checkIfRoomExists(roomName);
        if (indexOfRoom == -1){
            throw new RoomNotFoundException("Could not find room " + roomName);
        }

        return rooms[indexOfRoom].getReviews();
    }

    @Override
    public Team getTopTeamByRating() {
        if (teams == null || teams.length == 0){
            return null;
        }

        double max = 0;
        Team te = teams[0];
        for (Team t: teams) {
            if (t.getRating() > max){
                max = t.getRating();
                te = t;
            }
        }
        return te;
    }
}
