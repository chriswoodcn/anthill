export const fallbackLng = "en";
export const languageList = [
  { code: "en", name: "English" },
  { code: "zh", name: "Chinese" },
  // { code: "da", name: "Danish" },
  // { code: "fr", name: "French" },
  // { code: "de", name: "German" },
  // { code: "el", name: "Greek" },
  // { code: "hu", name: "Hungarian" },
  // { code: "it", name: "Italian" },
  // { code: "ja", name: "Japanese" },
  // { code: "pl", name: "Polish" },
  // { code: "pt", name: "Portuguese" },
  // { code: "ru", name: "Russian" },
  // { code: "es", name: "Spanish" },
  // { code: "sv", name: "Swedish" },
  // { code: "tr", name: "Turkish" },
  // { code: "ae", name: "Arabic" },
];
export const languages = languageList.map((item) => item.code);
export const defaultNS = "translation";
export const cookieName = "i18next";

export function getOptions(lng = fallbackLng, ns = defaultNS) {
  return {
    // debug: true,
    supportedLngs: languages,
    // preload: languages,
    fallbackLng,
    lng,
    fallbackNS: defaultNS,
    defaultNS,
    ns,
  };
}
