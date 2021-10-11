#pragma once
#include "other_functions.h"
#include <iostream>
#include <mutex>
namespace other
{
    class Printer {
        static std::mutex mut;
        static std::ostream& out;
        static std::ostream& err;
        template<typename T, typename ... Types>
        static void print_args(std::ostream& stream, T value, Types ... args)
        {
            stream << value;
            print_args(stream, args...);
        }
        template<typename T>
        static void print_args(std::ostream& stream, T value)
        {
            stream << value;
        }
    public:

        template<typename T, typename ... Types>
        static void print(Color text, Color background, T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(text,background);
            print_args(out, value, args...);
            set_base_colors();
        }
        template<typename T, typename ... Types>
        static void print(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            print_args(out, value, args...);
        }
        template<typename T, typename ... Types>
        static void print_error(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(Color::RED, Color::BLACK);
            print_args(err, value, args...);
            err << std::endl;
            set_base_colors();
        }
        template<typename T, typename ... Types>
        static void println(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            print_args(out, value, args...);
            out << std::endl;
        }
        template<typename T, typename ... Types>
        static void println(Color text, Color background, T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(text,background);
            print_args(out, value, args...);
            out << std::endl;
            set_base_colors();
        }
        static void set_base_colors()
        {
            set_color();
        }
    };
}