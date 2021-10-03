#include "Server/my_server.h"
#include <future>
void run()
{
    ms::MyServer server{"192.168.1.4",27015};
    server.run();
}
int main()
{
    auto fut = std::async(std::launch::async, run);
    fut.get();
    std::cout << "End" << std::endl;
    return 0;
}