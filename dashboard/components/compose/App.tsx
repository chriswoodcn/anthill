"use client";

import { PropsWithChildren, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { IRootState } from "@/store";
import {
  toggleRTL,
  toggleTheme,
  toggleMenu,
  toggleLayout,
  toggleAnimation,
} from "@/store/settingConfigSlice";

export default ({ children }: PropsWithChildren) => {
  const settingConfig = useSelector((state: IRootState) => state.settingConfig);
  const dispatch = useDispatch();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    dispatch(toggleTheme(localStorage.getItem("theme") || settingConfig.theme));
    dispatch(toggleMenu(localStorage.getItem("menu") || settingConfig.menu));
    dispatch(
      toggleLayout(localStorage.getItem("layout") || settingConfig.layout)
    );
    dispatch(
      toggleRTL(localStorage.getItem("rtlClass") || settingConfig.rtlClass)
    );
    dispatch(
      toggleAnimation(
        localStorage.getItem("animation") || settingConfig.animation
      )
    );

    setIsLoading(false);
  }, [
    dispatch,
    settingConfig.theme,
    settingConfig.menu,
    settingConfig.layout,
    settingConfig.rtlClass,
    settingConfig.animation,
    settingConfig.locale,
  ]);

  return (
    <div
      className={`${(settingConfig.sidebar && "toggle-sidebar") || ""} ${
        settingConfig.menu
      } ${settingConfig.layout} ${
        settingConfig.rtlClass
      } main-section relative font-nunito text-sm font-normal antialiased`}
    >
      {children}
    </div>
  );
};
