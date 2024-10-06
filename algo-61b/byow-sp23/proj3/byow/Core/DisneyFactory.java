package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DisneyFactory {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 45;
    private static final int MaxRoom = 25;
    private static final int MinRoom = 18;
    private static final int MinRoomHeight = 5;
    private static final int MinRoomWidth = 5;
    private static final int MaxRoomHeight = 10;
    private static final int MaxRoomWidth = 15;
    public TETile[][] disneyland = new TETile[WIDTH][HEIGHT];
    public static Random fairy;

    public DisneyFactory(String input) {
        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            numbers.append(input.charAt(i));
        }
        Long seed = Long.parseLong(numbers.toString());
        fairy = new Random(seed);
    }

    public void fillWithNothing() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                disneyland[x][y] = Tileset.NOTHING;
            }
        }
    }

    public ArrayList<Room> createRooms() {
        int roomCount = fairy.nextInt(MaxRoom - MinRoom) + MinRoom;
        int created = 0;
        ArrayList<Room> roomList = new ArrayList<>();
        while (created < roomCount) {
            int height = fairy.nextInt(MaxRoomHeight - MinRoomHeight) + MinRoomHeight;
            int width = fairy.nextInt(MaxRoomWidth - MinRoomWidth) + MinRoomWidth;
            int xCord = fairy.nextInt(WIDTH - width);
            int yCord = fairy.nextInt(HEIGHT - height);
            Mark point = new Mark(xCord, yCord);
            Room Minnie = new Room(height, width, point);
            boolean add = true;
            if (created != 0) {
                for (Room room : roomList) {
                    if (room.checkOverlap(Minnie)) {
                        add = false;
                    }
                }
            }
            if (add) {
                roomList.add(Minnie);
                created += 1;
            }
        }
        Collections.sort(roomList);
        return roomList;
    }

    public ArrayList<Hallway> createHallways(ArrayList<Room> rooms) {
        ArrayList<Hallway> hallwayList = new ArrayList<>();
        for (int i = 0; i < rooms.size() - 1; i++) {
            Hallway curr = new Hallway(rooms.get(i).secretPoint, rooms.get(i + 1).secretPoint);
            hallwayList.add(curr);
        }
        return hallwayList;
    }

    public TETile[][] createWorld() {
        fillWithNothing();
        ArrayList<Room> roomList = createRooms();
        for (Room room : roomList) {
            room.buildWalls(disneyland, Tileset.WALL);
        }
        ArrayList<Hallway> hallwayList = createHallways(roomList);
        for (Hallway hw : hallwayList) {
            hw.buildWalls(disneyland, Tileset.WALL);
        }
        for (Room room : roomList) {
            room.buildFloors(disneyland, Tileset.FLOOR);
        }
        for (Hallway hw : hallwayList) {
            hw.buildFloors(disneyland, Tileset.FLOOR);
        }
        for (Room room : roomList) {
            room.createFlowers(disneyland, Tileset.FLOWER);
        }
        return disneyland;
    }
}
