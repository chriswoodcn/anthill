"use client";

import { AppProgressBar as ProgressBar } from "next-nprogress-bar";
import { PropsWithChildren } from "react";
import configuraton, { PrimaryColors } from "@/configuration.mjs";

export default ({ children }: PropsWithChildren) => {
  return (
    <>
      {children}
      <ProgressBar
        height="2px"
        color={PrimaryColors[configuraton.Colors.DefaultLevel]}
        options={{ showSpinner: false }}
        shallowRouting
      />
    </>
  );
};
