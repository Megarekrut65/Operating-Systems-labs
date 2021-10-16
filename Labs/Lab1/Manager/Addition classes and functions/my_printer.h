#pragma once
#include "other_functions.h"
#include <iostream>
#include <mutex>
namespace other
{
    /**
     * \brief Class for thread-safe outing data to console
     */
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
        /**
         *
         * @tparam T - any type to output it to console
         * @tparam Types - any types to output it's to console
         * @param text - color of text
         * @param background - color of background
         * @param value - value to output to console
         * @param args - values to output to console
         *
         * \brief Outputs all values to console and changes text and background colors
         */
        template<typename T, typename ... Types>
        static void print(Color text, Color background, T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(text,background);
            print_args(out, value, args...);
            set_base_colors();
        }
        /**
         *
         * @tparam T - any type to output it to console
         * @tparam Types - any types to output it's to console
         * @param value - value to output to console
         * @param args - values to output to console
         *
         * \brief Outputs all values to console
         */
        template<typename T, typename ... Types>
        static void print(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            print_args(out, value, args...);
        }
        /**
         *
         * @tparam T - any type to output it to console
         * @tparam Types - any types to output it's to console
         * @param value - value to output to console
         * @param args - values to output to console
         *
         * \brief Outputs all values to console with red text color and adds std::endl to the end
         */
        template<typename T, typename ... Types>
        static void print_error(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(Color::RED, Color::BLACK);
            print_args(err, value, args...);
            err << std::endl;
            set_base_colors();
        }
        /**
         *
         * @tparam T - any type to output it to console
         * @tparam Types - any types to output it's to console
         * @param value - value to output to console
         * @param args - values to output to console
         *
         * \brief Outputs all values to console and adds std::endl to the end
         */
        template<typename T, typename ... Types>
        static void println(T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            print_args(out, value, args...);
            out << std::endl;
        }
        /**
         *
         * @tparam T - any type to output it to console
         * @tparam Types - any types to output it's to console
         * @param text - color of text
         * @param background - color of background
         * @param value - value to output to console
         * @param args - values to output to console
         *
         * \brief Outputs all values to console and changes text and background colors and adds std::endl to the end
         */
        template<typename T, typename ... Types>
        static void println(Color text, Color background, T value, Types ... args)
        {
            std::lock_guard<std::mutex> lock(mut);
            set_color(text,background);
            print_args(out, value, args...);
            out << std::endl;
            set_base_colors();
        }
        /**
         * \brief Sets basic colors: text - white, background - black
         */
        static void set_base_colors()
        {
            set_color();
        }
    };
}