#pragma once
#include "my_data.h"
#include "Server/my_server.h"
#include "Process/my_process.h"
#include <future>
#include "Addition classes and functions/shared_value.h"
namespace mym
{
    class MyManager {
    private:
        static void print_res(const std::string& fun_name, ms::FunctionParam x, ms::FunctionResult* y);
        static ms::FunctionResult* calculate_function(ms::FunctionParam x, myshv::SharedValue<bool>& shared,
                                      const std::string& app_path, const std::string& ip, int port);
        static ms::FunctionResult* start_calculating(ms::MyServer& server, mp::MyProcess& process, ms::FunctionParam x);
    public:
        static void run(ms::FunctionParam x, myshv::SharedValue<bool>& shared);
    };
}

