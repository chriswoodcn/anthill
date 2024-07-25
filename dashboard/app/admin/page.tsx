import Link from "next/link";
import { Trans } from "react-i18next/TransWithoutContext";
import { Button } from "@mantine/core";
import { getServerTranslations } from "@/i18n/server";
import { LangSelect } from "./LangSelect";

export async function generateMetadata() {
  const { t } = await getServerTranslations();
  return {
    title: t("home_title"),
  };
}

export default async () => {
  const { t, i18n } = await getServerTranslations();
  const path = "/admin";
  return (
    <div className="bg-bg_login">
      <h1 className="text-center">{t("home_title")}</h1>
      <div className="flex flex-col justify-center items-center mt-10">
        <Button component={Link} href={`/${i18n.resolvedLanguage}/second`}>
          {t("to-second-page")}
        </Button>
        <Button
          className="mt-5"
          component={Link}
          href={`/${i18n.resolvedLanguage}/client`}
        >
          {t("to-client-page")}
        </Button>
      </div>
      <footer className="flex flex-col justify-center items-center mt-10">
        <Trans i18nKey="languageSwitcher" t={t}>
          Switch from <strong>{i18n.resolvedLanguage}</strong> to:{" "}
        </Trans>
        <LangSelect currentLanguage={i18n.resolvedLanguage} />
      </footer>
    </div>
  );
};
