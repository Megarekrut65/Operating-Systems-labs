#include "Process/my_process.h"
#include "Server/my_server.h"
#include "my_data.h"
#include <future>
ms::FunctionResult* run(ms::FunctionParam x, const std::string& app_path, const std::string& ip, int port)
{
    mp::MyProcess process{app_path};
    ms::MyServer server{ip, port};
    auto res = server.run(x);
    process.wait_for_close();
    return res;
}
int main()
{
    ms::FunctionParam x = 5;
    std::future<ms::FunctionResult*> fut =
            std::async(std::launch::async, run, x, myd::MyData::F_APP_PATH, myd::MyData::IP, myd::MyData::F_PORT);
    auto res = fut.get();
    if(res) std::cout << "f("<<x << ")=" << *res << std::endl;
    delete res;
    std::cout << "End" << std::endl;
    return 0;
}