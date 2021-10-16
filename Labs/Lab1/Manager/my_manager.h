#pragma once
#include "my_data.h"
#include "Server/my_server.h"
#include "Process/my_process.h"
#include <future>
#include "Addition classes and functions/shared_value.h"
#include "Addition classes and functions/other_functions.h"
namespace mym
{
    using other::Color;
    using other::Printer;
    class MyManager {
    private:
        static void print_fun(const std::string& fun_name, mys::FunctionParam x, mys::FunctionResult* y);
        static mys::FunctionResult* calculate_function(mys::FunctionParam x, myshv::SharedValue<bool>& shared,
                                                       const std::string& app_path, const std::string& ip, int port);
        static mys::FunctionResult* start_calculating(mys::MyServer& server, myp::MyProcess& process, mys::FunctionParam x);
    public:
        static void run(mys::FunctionParam x, myshv::SharedValue<bool>& shared);
    };
}

