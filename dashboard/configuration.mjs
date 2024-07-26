import Color from "color";
export const genColors = (color) => {
  const ins = Color(color);
  return [
    ins.lighten(0.6).hex(),
    ins.lighten(0.5).hex(),
    ins.lighten(0.4).hex(),
    ins.lighten(0.3).hex(),
    ins.lighten(0.2).hex(),
    ins.lighten(0.1).hex(),
    ins.hex(),
    ins.darken(0.1).hex(),
    ins.darken(0.2).hex(),
    ins.darken(0.3).hex(),
  ];
};
export const mixColor = (color, mix, ratio) => {
  return Color(color).mix(Color(mix), ratio).hex();
};
export const mixWhite = (color, ratio) => {
  return mixColor(color, "#ffffff", ratio);
};
export const mixBlack = (color, ratio) => {
  return mixColor(color, "#000000", ratio);
};
const configuraton = {
  BasePath: "/bmm-dev",
  PathAlias: {
    Admin: {
      Pattern: /^\/admin\//,
      Root: "/admin/index",
      Login: "/admin/login",
    },
  },
  Settings: {
    locale: "en", // en, da, de, el, es, fr, hu, it, ja, pl, pt, ru, sv, tr, zh
    theme: "light", // light, dark, auto
    menu: "vertical", // vertical, collapsible-vertical, horizontal
    layout: "full", // full, boxed-layout
    rtlClass: "ltr", // rtl, ltr
    animation: "", // animate__fadeIn, animate__fadeInDown, animate__fadeInUp, animate__fadeInLeft, animate__fadeInRight, animate__slideInDown, animate__slideInLeft, animate__slideInRight, animate__zoomIn
  },
  AuthField: "token",
  FontFamily: ["Nunito", "sans-serif"],
  Colors: {
    DefaultLevel: 7,
    Primary: "#1badaa",
    Secondary: "#018ACB",
    Success: "#3ab078",
    Danger: "#FF7878",
    Warning: "#FFD966",
    Info: "#DBA979",
    White: "#ffffff",
  },
};
export default configuraton;
export const PrimaryColors = genColors(configuraton.Colors.Primary);
export const WhiteColors = [
  mixBlack("#ffffff", 0.9),
  mixBlack("#ffffff", 0.8),
  mixBlack("#ffffff", 0.7),
  mixBlack("#ffffff", 0.6),
  mixBlack("#ffffff", 0.5),
  mixBlack("#ffffff", 0.4),
  mixBlack("#ffffff", 0.3),
  mixBlack("#ffffff", 0.2),
  mixBlack("#ffffff", 0.1),
  "#ffffff",
];
export const BlackColors = [
  mixWhite("#000000", 0.9),
  mixWhite("#000000", 0.8),
  mixWhite("#000000", 0.7),
  mixWhite("#000000", 0.6),
  mixWhite("#000000", 0.5),
  mixWhite("#000000", 0.4),
  mixWhite("#000000", 0.3),
  mixWhite("#000000", 0.2),
  mixWhite("#000000", 0.1),
  "#000000",
];
