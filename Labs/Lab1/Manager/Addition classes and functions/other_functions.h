#pragma once
#include <WS2tcpip.h>
namespace other
{
    enum class Color {
        BLACK=0,
        BLUE,
        GREEN,
        AQUA,
        RED,
        PURPLE,
        YELLOW,
        WHITE,
        GRAY
    };
    void set_color(int text, int background);
    void set_color(Color text, Color background);
}
