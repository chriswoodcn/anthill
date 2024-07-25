"use client";

import { languages } from "@/i18n/settings";
import { useRouter } from "next/navigation";
import i18next from "i18next";
import { Button } from "@mantine/core";

export const LangSelect = ({
  currentLanguage,
}: {
  currentLanguage: string | undefined;
}) => {
  const router = useRouter();

  const handleChangeLanguage = (e: any, lang: string) => {
    e.preventDefault();
    i18next.changeLanguage(lang);
    router.refresh();
  };
  return (
    <div>
      {languages.map((lang, index) => {
        if (lang === currentLanguage) {
          return null;
        }
        return (
          <Button
            key={lang}
            className="mx-5"
            onClick={(e) => handleChangeLanguage(e, lang)}
            type="button"
          >
            {lang}
          </Button>
        );
      })}
    </div>
  );
};
