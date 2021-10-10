#include "my_manager.h"
namespace mym
{

    mys::FunctionResult*
    MyManager::calculate_function(mys::FunctionParam x, myshv::SharedValue<bool>& shared,
                                  const std::string &app_path, const std::string &ip, int port) {
        myp::MyProcess process{app_path};
        mys::MyServer server{ip, port};
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

    void MyManager::run(mys::FunctionParam x, myshv::SharedValue<bool>& shared) {
        std::future<mys::FunctionResult*> f_fut =
                std::async(std::launch::async,MyManager::calculate_function, x,std::ref(shared),
                           myd::MyData::F_APP_PATH, myd::MyData::IP, myd::MyData::F_PORT);
        std::future<mys::FunctionResult*> g_fut =
                std::async(std::launch::async,MyManager::calculate_function, x,std::ref(shared),
                           myd::MyData::G_APP_PATH, myd::MyData::IP, myd::MyData::G_PORT);
        auto f_res = f_fut.get();
        print_res("f",x,f_res);
        auto g_res = g_fut.get();
        print_res("g",x,g_res);
        Printer::println("\nManager is closed.");
        shared.set_value(true);
    }

    void MyManager::print_res(const std::string& fun_name, mys::FunctionParam x, mys::FunctionResult* y) {
        if(y)
        {
            Printer::println(Color::PURPLE, Color::BLACK,"\n",fun_name,"(",x,")=",*y);
            delete y;
        }
        else
        {
            Printer::println(Color::PURPLE, Color::BLACK,
                             "\n",fun_name,"(",x,") is not calculated!");
        }
    }

    mys::FunctionResult *MyManager::start_calculating(mys::MyServer &server,
                                                      myp::MyProcess &process, mys::FunctionParam x) {
        auto res = server.run(x);
        process.wait_for_close();
        return res;
    }
}