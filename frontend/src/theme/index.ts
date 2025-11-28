// src/theme/index.ts
import type { ThemeConfig } from "antd";

const theme: ThemeConfig = {
  token: {
    colorBgBase: "#0a0a0a",
    colorBgContainer: "#0f1115",
    colorBgElevated: "#101216",
    colorTextBase: "#e6eef8",
    colorTextSecondary: "#a9b0bb",
    colorBorder: "#26292f",
    colorPrimary: "#7b5cff",
    colorPrimaryHover: "#8f6bff",
    colorPrimaryActive: "#5e4bff",
    borderRadius: 8,
    fontFamily:
      "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans'",
    controlHeight: 40,
    boxShadow: "0 6px 20px rgba(11,10,20,0.6)",
    sizeUnit: 4,
  },

  components: {
    Layout: {
      colorBgHeader: "#0f1115",
      colorBgTrigger: "#0f1115",
      colorBgBody: "#09090b",
    },

    Card: {
      colorBgContainer: "#0f1115",
      boxShadow: "0 8px 30px rgba(11,10,20,0.6)",
      borderRadius: 10,
    },

    Button: {
      colorPrimary: "#7b5cff",
      colorPrimaryHover: "#8f6bff",
      colorPrimaryActive: "#5e4bff",
      colorTextLightSolid: "#ffffff",
      borderRadius: 999,
      paddingInline: 20,
      boxShadow: "0 6px 18px rgba(123,92,255,0.18)",
    },

    Input: {
      colorBgContainer: "#0f1115",
      colorBorder: "#22252a",
      colorBorderSecondary: "#2b2f36",
      colorText: "#e6eef8",
      borderRadius: 8,
      boxShadow: "inset 0 1px 0 rgba(255,255,255,0.02)",
    },

    List: {
      colorBgContainer: "#0f1115",
      colorBorder: "#1f2126",
    },

    Avatar: {
      colorBgContainer: "#111217",
    },

    Tooltip: {
      colorBgElevated: "#101013",
      boxShadowSecondary: "0 8px 24px rgba(0,0,0,0.45)",
      borderRadius: 8,
    },

    Tag: {
      colorPrimary: "#7b5cff",
      borderRadius: 999,
    },

    Badge: {
      colorBgContainer: "#ffffff12",
    },

    Typography: {
      colorText: "#e6eef8",
    },

    Menu: {
      colorBgElevated: "#0f1115",
      colorText: "#e6eef8",
      itemActiveBg: "#0e0d21",
    },

    Drawer: {
      colorBgElevated: "#0f1115",
    },

    Divider: {
      colorBorder: "#1f2126",
    },
  },
};

export default theme;
