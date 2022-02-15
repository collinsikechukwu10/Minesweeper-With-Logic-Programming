package core;

import java.util.Arrays;

public enum BoardCellType {
    HIDDEN('?',-3),
    MINE('m', -2),
    BLOCK('b', -1),
    MINE_NEIGBOUR_0('0', 0),
    MINE_NEIGBOUR_1('1', 1),
    MINE_NEIGBOUR_2('2', 2),
    MINE_NEIGBOUR_3('3', 3),
    MINE_NEIGBOUR_4('4', 4),
    MINE_NEIGBOUR_5('5', 5),
    MINE_NEIGBOUR_6('6', 6),
    MINE_NEIGBOUR_7('7', 7),
    MINE_NEIGBOUR_8('8', 8);


    private final char charValue;
    private final int intValue;

    BoardCellType(char charValue, int intValue) {

        this.charValue = charValue;
        this.intValue = intValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public static BoardCellType resolve(char c) {
        return Arrays.stream(BoardCellType.values()).filter(ct -> ct.getCharValue() == c).findFirst().orElse(null);
    }

}
