#include "my_manager.h"
#include <conio.h>
#include "Addition classes and functions/other_functions.h"
void user_menu(myshv::SharedValue<bool>& value);
using other::Color;
int main()
{
    other::set_color(Color::BLACK, Color::WHITE);
    std::cout << "Enter x: ";
    ms::FunctionParam x = 5;
    std::cin >> x;
    myshv::SharedValue<bool> value(false);
    std::future<void> fut =
            std::async(std::launch::async, mym::MyManager::run, x, std::ref(value));
    user_menu(value);
    return 0;
}
bool close_menu(){
    std::cout << "You definitely want to stop the program? Enter y(yes)/n(no).\n";
    char press = _getch();
    if(press == 'y') return true;
    return false;
}
void user_menu(myshv::SharedValue<bool>& value)
{
    std::cout << "Press Enter or Ctrl+c to stop calculation" << std::endl;
    while(true)
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
                std::cout << "Close..." << std::endl;
                break;
            }
            else std::cout << "Not close..." << std::endl;
        }
    }
}