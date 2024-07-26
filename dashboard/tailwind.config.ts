import { Config } from "tailwindcss";
import { withBasePath, } from "./lib";
import configuraton, { BlackColors, mixBlack, mixWhite, PrimaryColors, WhiteColors } from './configuration.mjs';
const levelDefault = configuraton.Colors.DefaultLevel
const config: Config = {
  darkMode: ["selector", "data-mantine-color-scheme"],
  content: [
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    container: {
      center: true,
    },
    extend: {
      fontFamily: {
        nunito: configuraton.FontFamily,
      },
      colors: {
        primary: {
          DEFAULT: PrimaryColors[levelDefault],
          0: PrimaryColors[0],
          1: PrimaryColors[1],
          2: PrimaryColors[2],
          3: PrimaryColors[3],
          4: PrimaryColors[4],
          5: PrimaryColors[5],
          6: PrimaryColors[6],
          7: PrimaryColors[7],
          8: PrimaryColors[8],
          9: PrimaryColors[9],
        },
        secondary: {
          DEFAULT: '#018ACB',
          light: '#018ACB77',
          'dark-light': 'rgb(128 93 202 / 15%)',
        },
        success: {
          DEFAULT: '#3ab078',
          light: '#3ab07877',
          'dark-light': 'rgb(58,176,120,.15)',
        },
        danger: {
          DEFAULT: '#FF7878',
          light: '#FF787877',
          'dark-light': 'rgb(255,120,120,.15)',
        },
        warning: {
          DEFAULT: '#FFD966',
          light: '#FFD96677',
          'dark-light': 'rgb(255,217,102,.15)',
        },
        info: {
          DEFAULT: '#DBA979',
          light: '#DBA979',
          'dark-light': 'rgb(219, 169, 121,.15)',
        },
        white: {
          DEFAULT: WhiteColors[9],
          0: WhiteColors[0],
          1: WhiteColors[1],
          2: WhiteColors[2],
          3: WhiteColors[3],
          4: WhiteColors[4],
          5: WhiteColors[5],
          6: WhiteColors[6],
          7: WhiteColors[7],
          8: WhiteColors[8],
          9: WhiteColors[9],
        },
        black: {
          DEFAULT: BlackColors[9],
          0: BlackColors[0],
          1: BlackColors[1],
          2: BlackColors[2],
          3: BlackColors[3],
          4: BlackColors[4],
          5: BlackColors[5],
          6: BlackColors[6],
          7: BlackColors[7],
          8: BlackColors[8],
          9: BlackColors[9],
        }
      },
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
        bg_login: `url('${withBasePath(
          "/assets/images/background/bg-login.jpg"
        )}')`,
        bg_map: `url('${withBasePath(
          "/assets/images/background/bg-map.png"
        )}')`,
      },
      backgroundColor: {
        "white-gray": mixBlack('#ffffff', 0.05),
        "black-gray": mixWhite('#000000', 0.05)
      }
    },
  },
  plugins: [],
};
export default config;
