import { subDomainList } from "./constant";

// this function helper.js helps to decide which router to use ..subdomain is the deciding factor
export const getApps = () => {
    const subdomain = getSubDomain(window.location.hostname);

    const mainApp = subDomainList.find((app) => app.main);
    if (subdomain === "") return mainApp.app;

    // this line is for checking if the subdomain is true 
    const apps = subDomainList.find((app) => subdomain === app.subdomain);

    return apps ? apps.app : mainApp.app;
}

// url.localhost..today is the url name 
// url.urlbestshort.com ....some future url if you buy
export const getSubDomain = (location) => {
    const locationParts = location.split(".");
    const isLocalhost = locationParts.slice(-1)[0] === "localhost";
    const sliceTill = isLocalhost ? -1 : -2;
    return locationParts.slice(0, sliceTill).join("");
};