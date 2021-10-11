#include "my_client.h"
#include <thread>
int main()
{
    mc::MyClient client{ "192.168.1.5",25565};
    std::this_thread::sleep_for(std::chrono::milliseconds(10000));
    client.run();
    return 0;
}
