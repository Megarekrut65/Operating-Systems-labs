#include "my_client.h"
#include <thread>
int main()
{
    static_assert(std::is_same<bool, os::lab1::compfuncs::op_group_traits<os::lab1::compfuncs::OR>::value_type>(), "wrong typing for OR");
    mc::MyClient client{ "192.168.1.5",25565};
    client.run();
    return 0;
}
