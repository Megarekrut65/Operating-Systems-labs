#include "my_manager.h"
namespace mym
{

    ms::FunctionResult*
    MyManager::calculate_function(ms::FunctionParam x, myshv::SharedValue<bool>& shared,
                                  const std::string &app_path, const std::string &ip, int port) {
        mp::MyProcess process{app_path};
        ms::MyServer server{ip, port};
        auto fut = std::async(std::launch::async, mym::MyManager::start_calculating, std::ref(server),
                              std::ref(process), x);
        while(fut.wait_for(std::chrono::milliseconds(100)) != std::future_status::ready)
        {
            if(shared.get_value())
            {
                process.close();
                server.close();
                return nullptr;
            }
        }
        return fut.get();
    }

    void MyManager::run(ms::FunctionParam x, myshv::SharedValue<bool>& shared) {
        std::future<ms::FunctionResult*> f_fut =
                std::async(std::launch::async,MyManager::calculate_function, x,std::ref(shared),
                           myd::MyData::F_APP_PATH, myd::MyData::IP, myd::MyData::F_PORT);
        std::future<ms::FunctionResult*> g_fut =
                std::async(std::launch::async,MyManager::calculate_function, x,std::ref(shared),
                           myd::MyData::G_APP_PATH, myd::MyData::IP, myd::MyData::G_PORT);
        auto f_res = f_fut.get();
        print_res("f",x,f_res);
        auto g_res = g_fut.get();
        print_res("g",x,g_res);
        std::cout <<"Manager is closed" << std::endl;
    }

    void MyManager::print_res(const std::string& fun_name, ms::FunctionParam x, ms::FunctionResult* y) {
        if(y)
        {
            std::cout << fun_name<<"("<<x<<")="<<*y << std::endl;
            delete y;
        }
    }

    ms::FunctionResult *MyManager::start_calculating(ms::MyServer &server,
                                                     mp::MyProcess &process, ms::FunctionParam x) {
        auto res = server.run(x);
        process.wait_for_close();
        return res;
    }
}