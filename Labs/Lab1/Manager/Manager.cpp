#include <cstdio>
#include "my_process.h"
#include <future>
#include <thread>
#include <queue>
#include <iostream>
using namespace mp;

FunctionResult start_function(const std::string& app_path, int port, int x)
{
    MyProcess process{app_path,port};
    return process.get_result();
}
int main()
{
    char f_path[] = R"(C:\Users\User81\source\repos\Megarekrut65\Operating-Systems-labs\Labs\Lab1\FirstFunction\Debug\FirstFunction.exe)";
    char g_path[] = R"(C:\Users\User81\source\repos\Megarekrut65\Operating-Systems-labs\Labs\Lab1\SecondFunction\Debug\SecondFunction.exe)";
    std::future<FunctionResult> f = std::async(std::launch::async, start_function, f_path, 2000, 10);
    std::future<FunctionResult> g = std::async(std::launch::async, start_function, g_path, 1500, 10);
    std::cout << "Start" << std::endl;
    std::cout << "f(x)=" << g.get() << std::endl;
    std::cout << "g(x)=" << f.get() << std::endl;
    return 0;
}
