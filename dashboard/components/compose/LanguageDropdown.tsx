"use client";

import PerfectScrollbar from "react-perfect-scrollbar";
import { IconChevronDown } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { IRootState } from "../../store";
import { toggleRTL } from "../../store/settingConfigSlice";
import { useRouter } from "next/navigation";
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { withBasePath } from "../../lib";
import Dropdown from '../core/Dropdown';

interface LanguageDropdownProps {
  className?: string;
}

const LanguageDropdown = ({ className = "" }: LanguageDropdownProps) => {
  const dispatch = useDispatch();
  const router = useRouter();
  const { i18n } = useTranslation();

  const isRtl =
    useSelector((state: IRootState) => state.settingConfig.rtlClass) === "rtl";

  const themeConfig = useSelector((state: IRootState) => state.settingConfig);
  const setLocale = (flag: string) => {
    if (flag.toLowerCase() === "ae") {
      dispatch(toggleRTL("rtl"));
    } else {
      dispatch(toggleRTL("ltr"));
    }
    router.refresh();
  };

  return (
    <div className={`dropdown ${className}`}>
      {i18n.language && (
        <Dropdown
          offset={[0, 4]}
          placement={`${isRtl ? "bottom-start" : "bottom-end"}`}
          btnClassName="flex items-center gap-2 rounded-lg border border-white-dark/30 bg-white px-2 py-1.5 text-white-dark hover:border-primary hover:text-primary dark:bg-black"
          button={
            <>
              <div>
                <img
                  src={withBasePath(
                    `/assets/images/flags/${i18n.language.toUpperCase()}.svg`
                  )}
                  alt="image"
                  className="h-5 w-5 rounded-full object-cover"
                />
              </div>
              <div className="text-base font-bold uppercase">
                {i18n.language}
              </div>
              <span className="shrink-0">
                <IconChevronDown stroke={1} size={20} />
              </span>
            </>
          }
        >
          <PerfectScrollbar className="max-h-[300px] overflow-y-scroll rounded-lg shadow-sm bg-white dark:bg-black">
            <ul className="grid w-[160px] grid-cols-1 gap-2 p-2 font-semibold text-dark dark:text-white-dark dark:text-white-light/90 ">
              {themeConfig.languageList.map((item: any) => {
                return (
                  <li key={item.code}>
                    <button
                      type="button"
                      className={`flex w-full p-2 rounded-md hover:text-primary ${
                        i18n.language === item.code
                          ? "bg-primary/10 text-primary"
                          : ""
                      }`}
                      onClick={() => {
                        i18n.changeLanguage(item.code);
                        setLocale(item.code);
                      }}
                    >
                      <img
                        src={withBasePath(
                          `/assets/images/flags/${item.code.toUpperCase()}.svg`
                        )}
                        alt="flag"
                        className="h-5 w-5 rounded-full object-cover"
                      />
                      <span className="ltr:ml-3 rtl:mr-3">{item.name}</span>
                    </button>
                  </li>
                );
              })}
            </ul>
          </PerfectScrollbar>
        </Dropdown>
      )}
    </div>
  );
};

export default LanguageDropdown;
