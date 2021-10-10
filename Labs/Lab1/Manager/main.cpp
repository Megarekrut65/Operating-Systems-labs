#include "my_manager.h"
#include <conio.h>
bool close_menu();
void user_menu(myshv::SharedValue<bool>& value);
using other::Color;
using other::Printer;
int main()
{
    Printer::set_base_colors();
    Printer::print("Enter x: ");
    mys::FunctionParam x = 5;
    std::cin >> x;
    myshv::SharedValue<bool> value(false);
    std::future<void> fut =
            std::async(std::launch::async, mym::MyManager::run, x, std::ref(value));
    user_menu(value);
    return 0;
}
bool close_menu()
{
    Printer::println(Color::YELLOW, Color::BLACK,
                     "You definitely want to stop the program? Enter y(yes)/n(no).");
    char press = _getch();
    if(press != 13) char enter=_getch();
    if(press == 'y') return true;
    return false;
}
void user_menu(myshv::SharedValue<bool>& value)
{
    Printer::print("Press Enter or Ctrl+c to stop calculation.");
    while(!value.get_value())
    {
        if(_kbhit() && _getch() == 13)
        {
            bool to_close = false;
            auto fut = std::async(std::launch::async,&close_menu);
            if(fut.wait_for(std::chrono::milliseconds(5000)) ==
                                                    std::future_status::ready)
            {
                to_close = fut.get();
            }
            if(to_close)
            {
                value.set_value(true);
                Printer::println(Color::YELLOW, Color::BLACK,
                                 "Close...");
                break;
            }
            else Printer::println(Color::YELLOW, Color::BLACK,"Not close...");
        }
    }
}