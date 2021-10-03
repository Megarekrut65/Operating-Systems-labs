#include "my_client.h"
int main()
{
    mc::MyClient client{ "192.168.1.4",27015 };
    client.run();
    return 0;
}