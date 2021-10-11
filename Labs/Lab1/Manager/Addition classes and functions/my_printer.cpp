#include "my_printer.h"

namespace other{
    std::mutex Printer::mut;
    std::ostream& Printer::out = std::cout;
    std::ostream& Printer::err = std::cerr;
}