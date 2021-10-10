#include "other_functions.h"

namespace other
{
    void set_color(Color text, Color background)
    {
        set_color(static_cast<int>(text), static_cast<int>(background));
    }
    void set_color(int text, int background)
    {
        HANDLE hStdOut = GetStdHandle(STD_OUTPUT_HANDLE);
        SetConsoleTextAttribute(hStdOut, (WORD)((background << 4) | text));
    }
}
