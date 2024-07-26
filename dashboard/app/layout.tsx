// third party
import "@mantine/core/styles.css";
import "@/styles/tailwind.css";
import "react-perfect-scrollbar/dist/css/styles.css";
import { ColorSchemeScript, MantineProvider } from "@mantine/core";
// i18next
import i18next, { dir } from "i18next";
import { detectLanguage, getServerTranslations } from "@/i18n/server";
import { I18nProvider } from "@/i18n/i18n-context";
// custom
import { withBasePath } from "@/lib";
import ComposedProvider from "@/components/ComposedProvider";
import configuraton, { PrimaryColors } from "@/configuration.mjs";

export async function generateMetadata() {
  const { t } = await getServerTranslations();
  return {
    title: t("title"),
    description: t("description"),
  };
}

export default async function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const lng = await detectLanguage();
  return (
    <html lang={lng} dir={dir(lng)}>
      <head>
        <link rel="icon" href={withBasePath("/assets/anthill.svg")} />
        <ColorSchemeScript />
      </head>
      <body>
        <I18nProvider language={lng}>
          <MantineProvider
            theme={{
              primaryColor: "anthill-primary",
              fontFamily: configuraton.FontFamily.join(","),
              colors: {
                "anthill-primary": PrimaryColors as any,
              },
            }}
          >
            <ComposedProvider>{children}</ComposedProvider>
          </MantineProvider>
        </I18nProvider>
      </body>
    </html>
  );
}
