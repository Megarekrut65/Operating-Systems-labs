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
    /**
     *
     * @param text - color of text in console
     * @param background - color of background in console
     *
     * \brief Sets text and background colors
     */
    void set_color(int text, int background);
    /**
     *
     * @param text - color of text in console
     * @param background - color of background in console
     *
     * \brief Sets text and background colors
     */
    void set_color(Color text = Color::WHITE, Color background = Color::BLACK);
}
