import React from "react";

export default () => {
  return (
    <div className="screen_loader animate__animated fixed inset-0 z-[60] grid place-content-center bg-white-gray dark:bg-black-gray">
      <div className="anthill-screen_loader-instance">
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>
  );
};
