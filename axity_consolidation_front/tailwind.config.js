/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Poppins', 'Arial', 'sans-serif'],
        serif: ['Georgia', 'serif'],
      },
      colors: {
        blue_primary: '#0F385A',        // Blue primario
        blue_secondary: '#1FB2DE',      // Blue secundario
        blue_alternative: '#007BA4',    // Blue alternativo
        blue_light_dark: '#B4D8FD',     // Blue light primario
        blue_light_main: '#E6EFFD',     // Blue light secundario
        blue_light: '#F0F8FF',          // Blue light alternativo
        magenta: '#E0006E',             // Color de acento
        magenta_dark: '#890646',        // Color de secundario
        gris: '#8798A7',                // Color gris
        amarillo: '#FBAF17',            // Color amarillo
      }
    },
  },
  plugins: [],
}

