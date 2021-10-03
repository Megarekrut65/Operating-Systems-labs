#include "my_process.h"
#include <future>
ms::FunctionResult* run(ms::FunctionParam x, char path[])
{
    mp::MyProcess process{std::string(path),"192.168.1.4",27015};
    return process.get_result(x);
}
int main()
{
    ms::FunctionParam x = 10;
    char f_path[]=R"(C:\Users\User81\source\repos\Megarekrut65\Operating-Systems-labs\Labs\Lab1\FirstFunction\Debug\FirstFunction.exe)";
    std::future<ms::FunctionResult*> fut =
            std::async(std::launch::async, run, x, f_path);
    auto res = fut.get();
    if(res) std::cout << "f("<<x << ")=" << *res << std::endl;
    std::cout << "End" << std::endl;
    return 0;
}