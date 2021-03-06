#pragma once
#include <string>
namespace myd
{
    /**
     * \brief Data's with paths, ips and ports for program
     */
    class MyData {
    public:
        static const std::string F_APP_PATH;
        static const std::string G_APP_PATH;
        static const std::string IP;
        static const int F_PORT;
        static const int G_PORT;
    };
}
