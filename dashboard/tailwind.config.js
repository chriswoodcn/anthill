import { withBasePath } from "./lib";
module.exports = {
  darkMode: ["selector", "data-mantine-color-scheme"],
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
        bg_login: `url('${withBasePath(
          "/assets/images/background/bg-login.jpg"
        )}')`,
      },
    },
  },
  plugins: [],
};
