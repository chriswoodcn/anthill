import React from "react";
// custom
import { getServerTranslations } from "@/i18n/server";
import LanguageDropdown from "@/components/compose/LanguageDropdown";
import LoginForm from "./_client/LoginForm";
import Logo from "@/components/compose/Logo";
import Image from "@/components/core/Image";

export async function generateMetadata() {
  const { t } = await getServerTranslations("admin_login");
  return {
    title: t("title"),
  };
}

export default async () => {
  const { t } = await getServerTranslations("admin_login");
  return (
    <div>
      <div className="absolute inset-0">
        <Image
          src="/assets/images/auth/bg-gradient.png"
          alt=""
          className="h-full w-full object-cover"
        />
      </div>

      <div className="relative flex min-h-screen items-center justify-center bg-bg_map bg-cover bg-center bg-no-repeat px-6 py-10 dark:bg-[#060818] sm:px-16">
        <Image
          src="/assets/images/auth/coming-soon-object1.png"
          alt=""
          className="absolute left-0 top-1/2 h-full max-h-[893px] -translate-y-1/2"
        />
        <Image
          src="/assets/images/auth/coming-soon-object2.png"
          alt=""
          className="absolute left-24 top-0 h-40 md:left-[30%]"
        />
        <Image
          src="/assets/images/auth/coming-soon-object3.png"
          alt=""
          className="absolute right-0 top-0 h-[300px]"
        />
        <Image
          src="assets/images/auth/polygon-object.svg"
          alt=""
          className="absolute bottom-0 end-[28%]"
        />
        <div className="relative w-full max-w-[870px] rounded-md bg-[linear-gradient(45deg,#fff9f9_0%,rgba(255,255,255,0)_25%,rgba(255,255,255,0)_75%,_#fff9f9_100%)] p-2 dark:bg-[linear-gradient(52.22deg,#0E1726_0%,rgba(14,23,38,0)_18.66%,rgba(14,23,38,0)_51.04%,rgba(14,23,38,0)_80.07%,#0E1726_100%)]">
          <div className="relative flex flex-col justify-center rounded-md bg-white/60 px-6 py-10 pt-16 backdrop-blur-lg dark:bg-black/50 lg:min-h-[480px]">
            <div className="absolute end-6 top-6">
              <LanguageDropdown />
            </div>
            <div className="flex justify-center items-center mb-5">
              <Logo className="w-16 h-16 md:w-24 md:h-24  text-primary" />
            </div>
            <div className="mx-auto w-full max-w-[440px]">
              <div className="mb-5 space-y-2">
                <h1 className="text-3xl font-bold uppercase !leading-snug text-primary md:text-4xl">
                  {t("title")}
                </h1>
                <p className="text-base font-thin leading-normal text-white-4">
                  {t("desc")}
                </p>
              </div>
              <LoginForm />
            </div>
          </div>
        </div>
        <p className="absolute bottom-2 w-full px-10 text-center  text-xs text-white-2 dark:text-black-2">
          &copy; {new Date().getFullYear()} TaoChen Electronic Technology Co.,
          Ltd All Rights Reserved.
        </p>
      </div>
    </div>
  );
};
