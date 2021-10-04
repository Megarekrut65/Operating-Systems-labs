#include "my_manager.h"
#include <conio.h>

void user_menu(myshv::SharedValue<bool>& value);
int main()
{
    std::cout << "Enter x: ";
    ms::FunctionParam x = 5;
    std::cin >> x;
    myshv::SharedValue<bool> value(false);
    std::future<void> fut =
            std::async(std::launch::async, mym::MyManager::run, x, std::ref(value));
    user_menu(value);
    return 0;
}
void user_menu(myshv::SharedValue<bool>& value)
{
    std::cout << "Press Enter or Ctrl+c to stop calculation" << std::endl;
    while(true)
    {
        if(_kbhit() && _getch() == 13)
        {
            std::cout << "You definitely want to stop the program? Enter y(yes)/n(no).\n";
            char press = _getch();
            if(press == 'n') continue;
            else if(press == 'y')
            {
                value.set_value(true);
                std::cout << "Close" << std::endl;
                break;
            }
            std::cout << "....." << std::endl;
        }
    }
}