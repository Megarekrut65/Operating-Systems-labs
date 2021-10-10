#include "my_client.h"
#include <thread>
int main()
{
    mc::MyClient client{ "192.168.1.4",27015 };
    std::this_thread::sleep_for(std::chrono::milliseconds(20000));
    client.run();
    return 0;
}