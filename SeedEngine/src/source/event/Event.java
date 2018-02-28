package source.event;

import org.lwjgl.glfw.GLFW;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Event {
    protected final int eventId;
    //Game events
    public static final Event GAME_TICK = new Event(0); //0x0000 (0)
    public static final Event BEGIN_PLAY = new Event(1); //0x0001 (1)
    public static final Event END_PLAY = new Event(2); //0x0002 (2)
    public static final Event GAME_PAUSE = new Event(3); //0x0003 (3)
    public static final Event GAME_UNPAUSE = new Event(4); //0x0003 (4)

    //Window Events
    public static final Event WINDOW_RESIZED = new Event(10); //0x000A (10)

    //Mouse events
    public static final Event MousePress_L = new Event(13); //0x000D (13)
    public static final Event MousePress_R = new Event(14); //0x000E (14)
    public static final Event MousePress_M = new Event(15); //0x000F (15)

    public static final Event MouseRelease_L = new Event(16); //0x0010 (16)
    public static final Event MouseRelease_R = new Event(17); //0x0011 (17)
    public static final Event MouseRelease_M = new Event(18); //0x0012 (18)

    public static final Event MouseDown_L = new Event(19); //0x0013 (19)
    public static final Event MouseDown_R = new Event(20); //0x0014 (20)
    public static final Event MouseDown_M = new Event(21); //0x0015 (21)

    //Mouse move events
    public static final Event MouseMove_X = new Event(22); //0x0016 (22)
    public static final Event MouseMove_Y = new Event(23); //0x0017 (23)

    public static double MousePos_X = 0; //Current Mouse position in X
    public static double MousePos_Y = 0; //Current Mouse position in Y
    public static double MousePos_dX = 0; //Current Mouse Movement in X
    public static double MousePos_dY = 0; //Current Mouse Movement in Y

    //Mouse scroll
    public static final Event MouseScroll = new Event(24); //0x0018 (24)
    public static double MouseScroll_X = 0; //Current Mouse scroll in X
    public static double MouseScroll_Y = 0; //Current Mouse scroll in Y

    //Mouse enter
    public static final Event MouseExit = new Event(25); //0x0019 (25)
    public static final Event MouseEnter = new Event(26); //0x001A (26)

    //Key Press
    public static final Event KeyPress_SPACE = new Event(GLFW.GLFW_KEY_SPACE); //0x0020 (32)

    public static final Event KeyPress_APOSTROPHE = new Event(GLFW.GLFW_KEY_APOSTROPHE); //0x0027 (39)
    public static final Event KeyPress_COMMA = new Event(GLFW.GLFW_KEY_COMMA); //0x002C (44)
    public static final Event KeyPress_PERIOD = new Event(GLFW.GLFW_KEY_PERIOD); //0x002E (46)

    public static final Event KeyPress_0 = new Event(GLFW.GLFW_KEY_0); //0x0030 (48)
    public static final Event KeyPress_1 = new Event(GLFW.GLFW_KEY_1); //0x0031 (49)
    public static final Event KeyPress_2 = new Event(GLFW.GLFW_KEY_2); //0x0032 (50)
    public static final Event KeyPress_3 = new Event(GLFW.GLFW_KEY_3); //0x0033 (51)
    public static final Event KeyPress_4 = new Event(GLFW.GLFW_KEY_4); //0x0034 (52)
    public static final Event KeyPress_5 = new Event(GLFW.GLFW_KEY_5); //0x0035 (53)
    public static final Event KeyPress_6 = new Event(GLFW.GLFW_KEY_6); //0x0036 (54)
    public static final Event KeyPress_7 = new Event(GLFW.GLFW_KEY_7); //0x0037 (55)
    public static final Event KeyPress_8 = new Event(GLFW.GLFW_KEY_8); //0x0038 (56)
    public static final Event KeyPress_9 = new Event(GLFW.GLFW_KEY_9); //0x0039 (57)
    public static final Event KeyPress_SEMICOLON = new Event(GLFW.GLFW_KEY_SEMICOLON); //0x003B (59)
    public static final Event KeyPress_EQUAL = new Event(GLFW.GLFW_KEY_EQUAL); //0x003D (61)
    public static final Event KeyPress_A = new Event(GLFW.GLFW_KEY_A); //0x0041 (65)
    public static final Event KeyPress_B = new Event(GLFW.GLFW_KEY_B); //0x0042 (66)
    public static final Event KeyPress_C = new Event(GLFW.GLFW_KEY_C); //0x0043 (67)
    public static final Event KeyPress_D = new Event(GLFW.GLFW_KEY_D); //0x0044 (68)
    public static final Event KeyPress_E = new Event(GLFW.GLFW_KEY_E); //0x0045 (69)
    public static final Event KeyPress_F = new Event(GLFW.GLFW_KEY_F); //0x0046 (70)
    public static final Event KeyPress_G = new Event(GLFW.GLFW_KEY_G); //0x0047 (71)
    public static final Event KeyPress_H = new Event(GLFW.GLFW_KEY_H); //0x0048 (72)
    public static final Event KeyPress_I = new Event(GLFW.GLFW_KEY_I); //0x0049 (73)
    public static final Event KeyPress_J = new Event(GLFW.GLFW_KEY_J); //0x004A (74)
    public static final Event KeyPress_K = new Event(GLFW.GLFW_KEY_K); //0x004B (75)
    public static final Event KeyPress_L = new Event(GLFW.GLFW_KEY_L); //0x004C (76)
    public static final Event KeyPress_M = new Event(GLFW.GLFW_KEY_M); //0x004D (77)
    public static final Event KeyPress_N = new Event(GLFW.GLFW_KEY_N); //0x004E (78)
    public static final Event KeyPress_O = new Event(GLFW.GLFW_KEY_O); //0x004F (79)
    public static final Event KeyPress_P = new Event(GLFW.GLFW_KEY_P); //0x0050 (80)
    public static final Event KeyPress_Q = new Event(GLFW.GLFW_KEY_Q); //0x0051 (81)
    public static final Event KeyPress_R = new Event(GLFW.GLFW_KEY_R); //0x0052 (82)
    public static final Event KeyPress_S = new Event(GLFW.GLFW_KEY_S); //0x0053 (83)
    public static final Event KeyPress_T = new Event(GLFW.GLFW_KEY_T); //0x0054 (84)
    public static final Event KeyPress_U = new Event(GLFW.GLFW_KEY_U); //0x0055 (85)
    public static final Event KeyPress_V = new Event(GLFW.GLFW_KEY_V); //0x0056 (86)
    public static final Event KeyPress_W = new Event(GLFW.GLFW_KEY_W); //0x0057 (87)
    public static final Event KeyPress_X = new Event(GLFW.GLFW_KEY_X); //0x0058 (88)
    public static final Event KeyPress_Y = new Event(GLFW.GLFW_KEY_Y); //0x0059 (89)
    public static final Event KeyPress_Z = new Event(GLFW.GLFW_KEY_Z); //0x005A (90)
    public static final Event KeyPress_LEFT_BRACKET = new Event(GLFW.GLFW_KEY_LEFT_BRACKET); //0x005B (91)
    public static final Event KeyPress_BACKSLASH = new Event(GLFW.GLFW_KEY_BACKSLASH); //0x005C (92)
    public static final Event KeyPress_RIGHT_BRACKET = new Event(GLFW.GLFW_KEY_RIGHT_BRACKET); //0x005D (93)
    public static final Event KeyPress_GRAVE_ACCENT = new Event(GLFW.GLFW_KEY_GRAVE_ACCENT); //0x0060 (96)

    public static final Event KeyPress_ESC = new Event(GLFW.GLFW_KEY_ESCAPE); //0x0100 (256)
    public static final Event KeyPress_ENTER = new Event(GLFW.GLFW_KEY_ENTER); //0x0101 (257)
    public static final Event KeyPress_TAB = new Event(GLFW.GLFW_KEY_TAB); //0x0102 (258)

    public static final Event KeyPress_RIGHT = new Event(GLFW.GLFW_KEY_RIGHT); //0x0106 (262)
    public static final Event KeyPress_LEFT = new Event(GLFW.GLFW_KEY_LEFT); //0x0107 (263)
    public static final Event KeyPress_DOWN = new Event(GLFW.GLFW_KEY_DOWN); //0x0108 (264)
    public static final Event KeyPress_UP = new Event(GLFW.GLFW_KEY_UP); //0x0109 (265)

    public static final Event KeyPress_F1 = new Event(GLFW.GLFW_KEY_F1); //0x0122 (290)
    public static final Event KeyPress_F2 = new Event(GLFW.GLFW_KEY_F2); //0x0123 (291)
    public static final Event KeyPress_F3 = new Event(GLFW.GLFW_KEY_F3); //0x0124 (292)
    public static final Event KeyPress_F4 = new Event(GLFW.GLFW_KEY_F4); //0x0125 (293)
    public static final Event KeyPress_F5 = new Event(GLFW.GLFW_KEY_F5); //0x0126 (294)
    public static final Event KeyPress_F6 = new Event(GLFW.GLFW_KEY_F6); //0x0127 (295)
    public static final Event KeyPress_F7 = new Event(GLFW.GLFW_KEY_F7); //0x0128 (296)
    public static final Event KeyPress_F8 = new Event(GLFW.GLFW_KEY_F8); //0x0129 (297)
    public static final Event KeyPress_F9 = new Event(GLFW.GLFW_KEY_F9); //0x012A (298)
    public static final Event KeyPress_F10 = new Event(GLFW.GLFW_KEY_F10); //0x012B (299)
    public static final Event KeyPress_F11 = new Event(GLFW.GLFW_KEY_F11); //0x012C (300)
    public static final Event KeyPress_F12 = new Event(GLFW.GLFW_KEY_F12); //0x012D (301)
    public static final Event KeyPress_F13 = new Event(GLFW.GLFW_KEY_F13); //0x012E (302)
    public static final Event KeyPress_F14 = new Event(GLFW.GLFW_KEY_F14); //0x012F (303)
    public static final Event KeyPress_F15 = new Event(GLFW.GLFW_KEY_F15); //0x0130 (304)
    public static final Event KeyPress_F16 = new Event(GLFW.GLFW_KEY_F16); //0x0131 (305)
    public static final Event KeyPress_F17 = new Event(GLFW.GLFW_KEY_F17); //0x0132 (306)
    public static final Event KeyPress_F18 = new Event(GLFW.GLFW_KEY_F18); //0x0133 (307)
    public static final Event KeyPress_F19 = new Event(GLFW.GLFW_KEY_F19); //0x0134 (308)
    public static final Event KeyPress_F20 = new Event(GLFW.GLFW_KEY_F20); //0x0135 (309)
    public static final Event KeyPress_F21 = new Event(GLFW.GLFW_KEY_F21); //0x0136 (310)
    public static final Event KeyPress_F22 = new Event(GLFW.GLFW_KEY_F22); //0x0137 (311)
    public static final Event KeyPress_F23 = new Event(GLFW.GLFW_KEY_F23); //0x0138 (312)
    public static final Event KeyPress_F24 = new Event(GLFW.GLFW_KEY_F24); //0x0139 (313)
    public static final Event KeyPress_F25 = new Event(GLFW.GLFW_KEY_F25); //0x013A (314)

    public static final Event KeyPress_LSHIFT = new Event(GLFW.GLFW_KEY_LEFT_SHIFT); //0x0154 (340)
    public static final Event KeyPress_RSHIFT = new Event(GLFW.GLFW_KEY_RIGHT_SHIFT); //0x0158 (344)

    //Key Release
    public static final Event KeyRelease_SPACE = new Event(GLFW.GLFW_KEY_SPACE + 0x8000); //0x8020 (0x8000 + 32)

    public static final Event KeyRelease_APOSTROPHE = new Event(GLFW.GLFW_KEY_APOSTROPHE + 0x8000); //0x8027 (0x8000 + 39)
    public static final Event KeyRelease_COMMA = new Event(GLFW.GLFW_KEY_COMMA + 0x8000); //0x802C (0x8000 + 44)
    public static final Event KeyRelease_PERIOD = new Event(GLFW.GLFW_KEY_PERIOD + 0x8000); //0x802E (0x8000 + 46)

    public static final Event KeyRelease_0 = new Event(GLFW.GLFW_KEY_0 + 0x8000); //0x8030 (0x8000 + 48)
    public static final Event KeyRelease_1 = new Event(GLFW.GLFW_KEY_1 + 0x8000); //0x8031 (0x8000 + 49)
    public static final Event KeyRelease_2 = new Event(GLFW.GLFW_KEY_2 + 0x8000); //0x8032 (0x8000 + 50)
    public static final Event KeyRelease_3 = new Event(GLFW.GLFW_KEY_3 + 0x8000); //0x8033 (0x8000 + 51)
    public static final Event KeyRelease_4 = new Event(GLFW.GLFW_KEY_4 + 0x8000); //0x8034 (0x8000 + 52)
    public static final Event KeyRelease_5 = new Event(GLFW.GLFW_KEY_5 + 0x8000); //0x8035 (0x8000 + 53)
    public static final Event KeyRelease_6 = new Event(GLFW.GLFW_KEY_6 + 0x8000); //0x8036 (0x8000 + 54)
    public static final Event KeyRelease_7 = new Event(GLFW.GLFW_KEY_7 + 0x8000); //0x8037 (0x8000 + 55)
    public static final Event KeyRelease_8 = new Event(GLFW.GLFW_KEY_8 + 0x8000); //0x8038 (0x8000 + 56)
    public static final Event KeyRelease_9 = new Event(GLFW.GLFW_KEY_9 + 0x8000); //0x8039 (0x8000 + 57)
    public static final Event KeyRelease_SEMICOLON = new Event(GLFW.GLFW_KEY_SEMICOLON + 0x8000); //0x803B (0x8000 + 59)
    public static final Event KeyRelease_EQUAL = new Event(GLFW.GLFW_KEY_EQUAL + 0x8000); //0x803D (0x8000 + 61)
    public static final Event KeyRelease_A = new Event(GLFW.GLFW_KEY_A + 0x8000); //0x8041 (0x8000 + 65)
    public static final Event KeyRelease_B = new Event(GLFW.GLFW_KEY_B + 0x8000); //0x8042 (0x8000 + 66)
    public static final Event KeyRelease_C = new Event(GLFW.GLFW_KEY_C + 0x8000); //0x8043 (0x8000 + 67)
    public static final Event KeyRelease_D = new Event(GLFW.GLFW_KEY_D + 0x8000); //0x8044 (0x8000 + 68)
    public static final Event KeyRelease_E = new Event(GLFW.GLFW_KEY_E + 0x8000); //0x8045 (0x8000 + 69)
    public static final Event KeyRelease_F = new Event(GLFW.GLFW_KEY_F + 0x8000); //0x8046 (0x8000 + 70)
    public static final Event KeyRelease_G = new Event(GLFW.GLFW_KEY_G + 0x8000); //0x8047 (0x8000 + 71)
    public static final Event KeyRelease_H = new Event(GLFW.GLFW_KEY_H + 0x8000); //0x8048 (0x8000 + 72)
    public static final Event KeyRelease_I = new Event(GLFW.GLFW_KEY_I + 0x8000); //0x8049 (0x8000 + 73)
    public static final Event KeyRelease_J = new Event(GLFW.GLFW_KEY_J + 0x8000); //0x804A (0x8000 + 74)
    public static final Event KeyRelease_K = new Event(GLFW.GLFW_KEY_K + 0x8000); //0x804B (0x8000 + 75)
    public static final Event KeyRelease_L = new Event(GLFW.GLFW_KEY_L + 0x8000); //0x804C (0x8000 + 76)
    public static final Event KeyRelease_M = new Event(GLFW.GLFW_KEY_M + 0x8000); //0x804D (0x8000 + 77)
    public static final Event KeyRelease_N = new Event(GLFW.GLFW_KEY_N + 0x8000); //0x804E (0x8000 + 78)
    public static final Event KeyRelease_O = new Event(GLFW.GLFW_KEY_O + 0x8000); //0x804F (0x8000 + 79)
    public static final Event KeyRelease_P = new Event(GLFW.GLFW_KEY_P + 0x8000); //0x8050 (0x8000 + 80)
    public static final Event KeyRelease_Q = new Event(GLFW.GLFW_KEY_Q + 0x8000); //0x8051 (0x8000 + 81)
    public static final Event KeyRelease_R = new Event(GLFW.GLFW_KEY_R + 0x8000); //0x8052 (0x8000 + 82)
    public static final Event KeyRelease_S = new Event(GLFW.GLFW_KEY_S + 0x8000); //0x8053 (0x8000 + 83)
    public static final Event KeyRelease_T = new Event(GLFW.GLFW_KEY_T + 0x8000); //0x8054 (0x8000 + 84)
    public static final Event KeyRelease_U = new Event(GLFW.GLFW_KEY_U + 0x8000); //0x8055 (0x8000 + 85)
    public static final Event KeyRelease_V = new Event(GLFW.GLFW_KEY_V + 0x8000); //0x8056 (0x8000 + 86)
    public static final Event KeyRelease_W = new Event(GLFW.GLFW_KEY_W + 0x8000); //0x8057 (0x8000 + 87)
    public static final Event KeyRelease_X = new Event(GLFW.GLFW_KEY_X + 0x8000); //0x8058 (0x8000 + 88)
    public static final Event KeyRelease_Y = new Event(GLFW.GLFW_KEY_Y + 0x8000); //0x8059 (0x8000 + 89)
    public static final Event KeyRelease_Z = new Event(GLFW.GLFW_KEY_Z + 0x8000); //0x805A (0x8000 + 90)
    public static final Event KeyRelease_LEFT_BRACKET = new Event(GLFW.GLFW_KEY_LEFT_BRACKET + 0x8000); //0x805B (0x8000 + 91)
    public static final Event KeyRelease_BACKSLASH = new Event(GLFW.GLFW_KEY_BACKSLASH + 0x8000); //0x805C (0x8000 + 92)
    public static final Event KeyRelease_RIGHT_BRACKET = new Event(GLFW.GLFW_KEY_RIGHT_BRACKET + 0x8000); //0x805D (0x8000 + 93)
    public static final Event KeyRelease_GRAVE_ACCENT = new Event(GLFW.GLFW_KEY_GRAVE_ACCENT + 0x8000); //0x8060 (0x8000 + 96)

    public static final Event KeyRelease_ESC = new Event(GLFW.GLFW_KEY_ESCAPE + 0x8000); //0x8100 (0x8000 + 256)
    public static final Event KeyRelease_ENTER = new Event(GLFW.GLFW_KEY_ENTER + 0x8000); //0x8101 (0x8000 + 257)
    public static final Event KeyRelease_TAB = new Event(GLFW.GLFW_KEY_TAB + 0x8000); //0x8102 (0x8000 + 258)

    public static final Event KeyRelease_RIGHT = new Event(GLFW.GLFW_KEY_RIGHT + 0x8000); //0x8106 (0x8000 + 262)
    public static final Event KeyRelease_LEFT = new Event(GLFW.GLFW_KEY_LEFT + 0x8000); //0x8107 (0x8000 + 263)
    public static final Event KeyRelease_DOWN = new Event(GLFW.GLFW_KEY_DOWN + 0x8000); //0x8108 (0x8000 + 264)
    public static final Event KeyRelease_UP = new Event(GLFW.GLFW_KEY_UP + 0x8000); //0x8109 (0x8000 + 265)

    public static final Event KeyRelease_F1 = new Event(GLFW.GLFW_KEY_F1 + 0x8000); //0x8122 (0x8000 + 290)
    public static final Event KeyRelease_F2 = new Event(GLFW.GLFW_KEY_F2 + 0x8000); //0x8123 (0x8000 + 291)
    public static final Event KeyRelease_F3 = new Event(GLFW.GLFW_KEY_F3 + 0x8000); //0x8124 (0x8000 + 292)
    public static final Event KeyRelease_F4 = new Event(GLFW.GLFW_KEY_F4 + 0x8000); //0x8125 (0x8000 + 293)
    public static final Event KeyRelease_F5 = new Event(GLFW.GLFW_KEY_F5 + 0x8000); //0x8126 (0x8000 + 294)
    public static final Event KeyRelease_F6 = new Event(GLFW.GLFW_KEY_F6 + 0x8000); //0x8127 (0x8000 + 295)
    public static final Event KeyRelease_F7 = new Event(GLFW.GLFW_KEY_F7 + 0x8000); //0x8128 (0x8000 + 296)
    public static final Event KeyRelease_F8 = new Event(GLFW.GLFW_KEY_F8 + 0x8000); //0x8129 (0x8000 + 297)
    public static final Event KeyRelease_F9 = new Event(GLFW.GLFW_KEY_F9 + 0x8000); //0x812A (0x8000 + 298)
    public static final Event KeyRelease_F10 = new Event(GLFW.GLFW_KEY_F10 + 0x8000); //0x812B (0x8000 + 299)
    public static final Event KeyRelease_F11 = new Event(GLFW.GLFW_KEY_F11 + 0x8000); //0x812C (0x8000 + 300)
    public static final Event KeyRelease_F12 = new Event(GLFW.GLFW_KEY_F12 + 0x8000); //0x812D (0x8000 + 301)
    public static final Event KeyRelease_F13 = new Event(GLFW.GLFW_KEY_F13 + 0x8000); //0x812E (0x8000 + 302)
    public static final Event KeyRelease_F14 = new Event(GLFW.GLFW_KEY_F14 + 0x8000); //0x812F (0x8000 + 303)
    public static final Event KeyRelease_F15 = new Event(GLFW.GLFW_KEY_F15 + 0x8000); //0x8130 (0x8000 + 304)
    public static final Event KeyRelease_F16 = new Event(GLFW.GLFW_KEY_F16 + 0x8000); //0x8131 (0x8000 + 305)
    public static final Event KeyRelease_F17 = new Event(GLFW.GLFW_KEY_F17 + 0x8000); //0x8132 (0x8000 + 306)
    public static final Event KeyRelease_F18 = new Event(GLFW.GLFW_KEY_F18 + 0x8000); //0x8133 (0x8000 + 307)
    public static final Event KeyRelease_F19 = new Event(GLFW.GLFW_KEY_F19 + 0x8000); //0x8134 (0x8000 + 308)
    public static final Event KeyRelease_F20 = new Event(GLFW.GLFW_KEY_F20 + 0x8000); //0x8135 (0x8000 + 309)
    public static final Event KeyRelease_F21 = new Event(GLFW.GLFW_KEY_F21 + 0x8000); //0x8136 (0x8000 + 310)
    public static final Event KeyRelease_F22 = new Event(GLFW.GLFW_KEY_F22 + 0x8000); //0x8137 (0x8000 + 311)
    public static final Event KeyRelease_F23 = new Event(GLFW.GLFW_KEY_F23 + 0x8000); //0x8138 (0x8000 + 312)
    public static final Event KeyRelease_F24 = new Event(GLFW.GLFW_KEY_F24 + 0x8000); //0x8139 (0x8000 + 313)
    public static final Event KeyRelease_F25 = new Event(GLFW.GLFW_KEY_F25 + 0x8000); //0x813A (0x8000 + 314)

    public static final Event KeyRelease_LSHIFT = new Event(GLFW.GLFW_KEY_LEFT_SHIFT + 0x8000); //0x8154 (0x8000 + 340)
    public static final Event KeyRelease_RSHIFT = new Event(GLFW.GLFW_KEY_RIGHT_SHIFT + 0x8000); //0x8158 (0x8000 + 344)

    //Key Down
    public static final Event KeyDown_SPACE = new Event(GLFW.GLFW_KEY_SPACE + 0xF000); //0xF020 (0xF000 + 32)

    public static final Event KeyDown_APOSTROPHE = new Event(GLFW.GLFW_KEY_APOSTROPHE + 0xF000); //0xF027 (0xF000 + 39)
    public static final Event KeyDown_COMMA = new Event(GLFW.GLFW_KEY_COMMA + 0xF000); //0xF02C (0xF000 + 44)
    public static final Event KeyDown_PERIOD = new Event(GLFW.GLFW_KEY_PERIOD + 0xF000); //0xF02E (0xF000 + 46)

    public static final Event KeyDown_0 = new Event(GLFW.GLFW_KEY_0 + 0xF000); //0xF030 (0xF000 + 48)
    public static final Event KeyDown_1 = new Event(GLFW.GLFW_KEY_1 + 0xF000); //0xF031 (0xF000 + 49)
    public static final Event KeyDown_2 = new Event(GLFW.GLFW_KEY_2 + 0xF000); //0xF032 (0xF000 + 50)
    public static final Event KeyDown_3 = new Event(GLFW.GLFW_KEY_3 + 0xF000); //0xF033 (0xF000 + 51)
    public static final Event KeyDown_4 = new Event(GLFW.GLFW_KEY_4 + 0xF000); //0xF034 (0xF000 + 52)
    public static final Event KeyDown_5 = new Event(GLFW.GLFW_KEY_5 + 0xF000); //0xF035 (0xF000 + 53)
    public static final Event KeyDown_6 = new Event(GLFW.GLFW_KEY_6 + 0xF000); //0xF036 (0xF000 + 54)
    public static final Event KeyDown_7 = new Event(GLFW.GLFW_KEY_7 + 0xF000); //0xF037 (0xF000 + 55)
    public static final Event KeyDown_8 = new Event(GLFW.GLFW_KEY_8 + 0xF000); //0xF038 (0xF000 + 56)
    public static final Event KeyDown_9 = new Event(GLFW.GLFW_KEY_9 + 0xF000); //0xF039 (0xF000 + 57)
    public static final Event KeyDown_SEMICOLON = new Event(GLFW.GLFW_KEY_SEMICOLON + 0xF000); //0xF03B (0xF000 + 59)
    public static final Event KeyDown_EQUAL = new Event(GLFW.GLFW_KEY_EQUAL + 0xF000); //0xF03D (0xF000 + 61)
    public static final Event KeyDown_A = new Event(GLFW.GLFW_KEY_A + 0xF000); //0xF041 (0xF000 + 65)
    public static final Event KeyDown_B = new Event(GLFW.GLFW_KEY_B + 0xF000); //0xF042 (0xF000 + 66)
    public static final Event KeyDown_C = new Event(GLFW.GLFW_KEY_C + 0xF000); //0xF043 (0xF000 + 67)
    public static final Event KeyDown_D = new Event(GLFW.GLFW_KEY_D + 0xF000); //0xF044 (0xF000 + 68)
    public static final Event KeyDown_E = new Event(GLFW.GLFW_KEY_E + 0xF000); //0xF045 (0xF000 + 69)
    public static final Event KeyDown_F = new Event(GLFW.GLFW_KEY_F + 0xF000); //0xF046 (0xF000 + 70)
    public static final Event KeyDown_G = new Event(GLFW.GLFW_KEY_G + 0xF000); //0xF047 (0xF000 + 71)
    public static final Event KeyDown_H = new Event(GLFW.GLFW_KEY_H + 0xF000); //0xF048 (0xF000 + 72)
    public static final Event KeyDown_I = new Event(GLFW.GLFW_KEY_I + 0xF000); //0xF049 (0xF000 + 73)
    public static final Event KeyDown_J = new Event(GLFW.GLFW_KEY_J + 0xF000); //0xF04A (0xF000 + 74)
    public static final Event KeyDown_K = new Event(GLFW.GLFW_KEY_K + 0xF000); //0xF04B (0xF000 + 75)
    public static final Event KeyDown_L = new Event(GLFW.GLFW_KEY_L + 0xF000); //0xF04C (0xF000 + 76)
    public static final Event KeyDown_M = new Event(GLFW.GLFW_KEY_M + 0xF000); //0xF04D (0xF000 + 77)
    public static final Event KeyDown_N = new Event(GLFW.GLFW_KEY_N + 0xF000); //0xF04E (0xF000 + 78)
    public static final Event KeyDown_O = new Event(GLFW.GLFW_KEY_O + 0xF000); //0xF04F (0xF000 + 79)
    public static final Event KeyDown_P = new Event(GLFW.GLFW_KEY_P + 0xF000); //0xF050 (0xF000 + 80)
    public static final Event KeyDown_Q = new Event(GLFW.GLFW_KEY_Q + 0xF000); //0xF051 (0xF000 + 81)
    public static final Event KeyDown_R = new Event(GLFW.GLFW_KEY_R + 0xF000); //0xF052 (0xF000 + 82)
    public static final Event KeyDown_S = new Event(GLFW.GLFW_KEY_S + 0xF000); //0xF053 (0xF000 + 83)
    public static final Event KeyDown_T = new Event(GLFW.GLFW_KEY_T + 0xF000); //0xF054 (0xF000 + 84)
    public static final Event KeyDown_U = new Event(GLFW.GLFW_KEY_U + 0xF000); //0xF055 (0xF000 + 85)
    public static final Event KeyDown_V = new Event(GLFW.GLFW_KEY_V + 0xF000); //0xF056 (0xF000 + 86)
    public static final Event KeyDown_W = new Event(GLFW.GLFW_KEY_W + 0xF000); //0xF057 (0xF000 + 87)
    public static final Event KeyDown_X = new Event(GLFW.GLFW_KEY_X + 0xF000); //0xF058 (0xF000 + 88)
    public static final Event KeyDown_Y = new Event(GLFW.GLFW_KEY_Y + 0xF000); //0xF059 (0xF000 + 89)
    public static final Event KeyDown_Z = new Event(GLFW.GLFW_KEY_Z + 0xF000); //0xF05A (0xF000 + 90)
    public static final Event KeyDown_LEFT_BRACKET = new Event(GLFW.GLFW_KEY_LEFT_BRACKET + 0xF000); //0xF05B (0xF000 + 91)
    public static final Event KeyDown_BACKSLASH = new Event(GLFW.GLFW_KEY_BACKSLASH + 0xF000); //0xF05C (0xF000 + 92)
    public static final Event KeyDown_RIGHT_BRACKET = new Event(GLFW.GLFW_KEY_RIGHT_BRACKET + 0xF000); //0xF05D (0xF000 + 93)
    public static final Event KeyDown_GRAVE_ACCENT = new Event(GLFW.GLFW_KEY_GRAVE_ACCENT + 0xF000); //0xF060 (0xF000 + 96)

    public static final Event KeyDown_ESC = new Event(GLFW.GLFW_KEY_ESCAPE + 0xF000); //0xF100 (0xF000 + 256)
    public static final Event KeyDown_ENTER = new Event(GLFW.GLFW_KEY_ENTER + 0xF000); //0xF101 (0xF000 + 257)
    public static final Event KeyDown_TAB = new Event(GLFW.GLFW_KEY_TAB + 0xF000); //0xF102 (0xF000 + 258)

    public static final Event KeyDown_RIGHT = new Event(GLFW.GLFW_KEY_RIGHT + 0xF000); //0xF106 (0xF000 + 262)
    public static final Event KeyDown_LEFT = new Event(GLFW.GLFW_KEY_LEFT + 0xF000); //0xF107 (0xF000 + 263)
    public static final Event KeyDown_DOWN = new Event(GLFW.GLFW_KEY_DOWN + 0xF000); //0xF108 (0xF000 + 264)
    public static final Event KeyDown_UP = new Event(GLFW.GLFW_KEY_UP + 0xF000); //0xF109 (0xF000 + 265)

    public static final Event KeyDown_F1 = new Event(GLFW.GLFW_KEY_F1 + 0xF000); //0xF122 (0xF000 + 290)
    public static final Event KeyDown_F2 = new Event(GLFW.GLFW_KEY_F2 + 0xF000); //0xF123 (0xF000 + 291)
    public static final Event KeyDown_F3 = new Event(GLFW.GLFW_KEY_F3 + 0xF000); //0xF124 (0xF000 + 292)
    public static final Event KeyDown_F4 = new Event(GLFW.GLFW_KEY_F4 + 0xF000); //0xF125 (0xF000 + 293)
    public static final Event KeyDown_F5 = new Event(GLFW.GLFW_KEY_F5 + 0xF000); //0xF126 (0xF000 + 294)
    public static final Event KeyDown_F6 = new Event(GLFW.GLFW_KEY_F6 + 0xF000); //0xF127 (0xF000 + 295)
    public static final Event KeyDown_F7 = new Event(GLFW.GLFW_KEY_F7 + 0xF000); //0xF128 (0xF000 + 296)
    public static final Event KeyDown_F8 = new Event(GLFW.GLFW_KEY_F8 + 0xF000); //0xF129 (0xF000 + 297)
    public static final Event KeyDown_F9 = new Event(GLFW.GLFW_KEY_F9 + 0xF000); //0xF12A (0xF000 + 298)
    public static final Event KeyDown_F10 = new Event(GLFW.GLFW_KEY_F10 + 0xF000); //0xF12B (0xF000 + 299)
    public static final Event KeyDown_F11 = new Event(GLFW.GLFW_KEY_F11 + 0xF000); //0xF12C (0xF000 + 300)
    public static final Event KeyDown_F12 = new Event(GLFW.GLFW_KEY_F12 + 0xF000); //0xF12D (0xF000 + 301)
    public static final Event KeyDown_F13 = new Event(GLFW.GLFW_KEY_F13 + 0xF000); //0xF12E (0xF000 + 302)
    public static final Event KeyDown_F14 = new Event(GLFW.GLFW_KEY_F14 + 0xF000); //0xF12F (0xF000 + 303)
    public static final Event KeyDown_F15 = new Event(GLFW.GLFW_KEY_F15 + 0xF000); //0xF130 (0xF000 + 304)
    public static final Event KeyDown_F16 = new Event(GLFW.GLFW_KEY_F16 + 0xF000); //0xF131 (0xF000 + 305)
    public static final Event KeyDown_F17 = new Event(GLFW.GLFW_KEY_F17 + 0xF000); //0xF132 (0xF000 + 306)
    public static final Event KeyDown_F18 = new Event(GLFW.GLFW_KEY_F18 + 0xF000); //0xF133 (0xF000 + 307)
    public static final Event KeyDown_F19 = new Event(GLFW.GLFW_KEY_F19 + 0xF000); //0xF134 (0xF000 + 308)
    public static final Event KeyDown_F20 = new Event(GLFW.GLFW_KEY_F20 + 0xF000); //0xF135 (0xF000 + 309)
    public static final Event KeyDown_F21 = new Event(GLFW.GLFW_KEY_F21 + 0xF000); //0xF136 (0xF000 + 310)
    public static final Event KeyDown_F22 = new Event(GLFW.GLFW_KEY_F22 + 0xF000); //0xF137 (0xF000 + 311)
    public static final Event KeyDown_F23 = new Event(GLFW.GLFW_KEY_F23 + 0xF000); //0xF138 (0xF000 + 312)
    public static final Event KeyDown_F24 = new Event(GLFW.GLFW_KEY_F24 + 0xF000); //0xF139 (0xF000 + 313)
    public static final Event KeyDown_F25 = new Event(GLFW.GLFW_KEY_F25 + 0xF000); //0xF13A (0xF000 + 314)

    public static final Event KeyDown_LSHIFT = new Event(GLFW.GLFW_KEY_LEFT_SHIFT + 0xF000); //0xF154 (0xF000 + 340)
    public static final Event KeyDown_RSHIFT = new Event(GLFW.GLFW_KEY_RIGHT_SHIFT + 0xF000); //0xF158 (0xF000 + 344)

    public Event(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return eventId == event.eventId;
    }

    @Override
    public int hashCode() {
        return eventId;
    }
}
