import { createSlice } from '@reduxjs/toolkit';
import configuration from '@/configuration.mjs';
import { languageList } from '@/i18n/settings';

const DefaultSettings = configuration.Settings

const initialState = {
  isDarkMode: false,
  sidebar: false,
  theme: DefaultSettings.theme,
  menu: DefaultSettings.menu,
  layout: DefaultSettings.layout,
  rtlClass: DefaultSettings.rtlClass,
  animation: DefaultSettings.animation,
  locale: DefaultSettings.locale,
  languageList
};

const settingConfigSlice = createSlice({
  name: 'settingConfigSlice',
  initialState: initialState,
  reducers: {
    toggleTheme(state, { payload }) {
      payload = payload || state.theme; // light | dark | auto
      localStorage.setItem('theme', payload);
      state.theme = payload;
      if (payload === 'light') {
        state.isDarkMode = false;
        document.querySelector('html')?.setAttribute("data-mantine-color-scheme", "light")
      } else if (payload === 'dark') {
        state.isDarkMode = true;
        document.querySelector('html')?.setAttribute("data-mantine-color-scheme", "dark")
      } else if (payload === 'auto') {
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
          state.isDarkMode = true;
          state.isDarkMode = false;
          document.querySelector('html')?.setAttribute("data-mantine-color-scheme", "dark")
        }
        document.querySelector('html')?.setAttribute("data-mantine-color-scheme", "auto")
      }
    },
    toggleMenu(state, { payload }) {
      payload = payload || state.menu; // vertical, collapsible-vertical, horizontal
      localStorage.setItem('menu', payload);
      state.menu = payload;
    },
    toggleLayout(state, { payload }) {
      payload = payload || state.layout; // full, boxed-layout
      localStorage.setItem('layout', payload);
      state.layout = payload;
    },
    toggleRTL(state, { payload }) {
      payload = payload || state.rtlClass; // rtl, ltr
      localStorage.setItem('rtlClass', payload);
      state.rtlClass = payload;
      document.querySelector('html')?.setAttribute('dir', state.rtlClass || 'ltr');
    },
    toggleAnimation(state, { payload }) {
      payload = payload || state.animation; // animate__fadeIn, animate__fadeInDown, animate__fadeInUp, animate__fadeInLeft, animate__fadeInRight, animate__slideInDown, animate__slideInLeft, animate__slideInRight, animate__zoomIn
      payload = payload?.trim();
      localStorage.setItem('animation', payload);
      state.animation = payload;
    },
    toggleSidebar(state) {
      state.sidebar = !state.sidebar;
    },
    resetToggleSidebar(state) {
      state.sidebar = false;
    },
  },
});

export const { toggleTheme, toggleMenu, toggleLayout, toggleRTL, toggleAnimation, toggleSidebar, resetToggleSidebar } = settingConfigSlice.actions;

export default settingConfigSlice.reducer;