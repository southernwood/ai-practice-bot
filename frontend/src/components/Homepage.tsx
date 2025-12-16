import { useState } from "react";
import { ChatBubble } from "./ChatBox/ChatBobble";
import { ChatDrawer } from "./ChatBox/ChatDrawer";
import { ContactBar } from "./ContactBar";
import { Footer } from "./Footer";
import { Timeline, type TimelineGroupType } from "./Timeline/Timeline";
import { Button } from "./ui/button";

const groups: TimelineGroupType[] = [
  {
    title: "Working Experience",
    linePosition: "left",
    dotColor: "#a855f7",
    dotBorderColor: "#9333ea",
    items: [
      {
        title: "Senior Software Engineer – eBay Inc.",
        time: "Jun 2020 – Present · Bellevue, WA",
        type: "work",
        summary: [
          "Full-stack engineer developing and maintaining internal platforms for Spark SQL execution, data pipelines, and user self-service tools.",
        ],
        details: [
          "Developed and maintained an internal Spark SQL platform: front-end with React, Node.js; back-end with Java Spring and MongoDB",
          "Managed job scheduling systems, initially running on eBay's proprietary UC4 scheduler; later designed and fully managed our own Airflow cluster, owning all components to improve reliability and scalability",
          "Implemented monitoring and alerting using Jenkins and Prometheus to ensure platform stability",
          "Built CI/CD pipelines with Jenkins and n8n for automated testing and deployment",
          "Leading the development of SprintAI, a self-service ML-powered tool for users to resolve issues and query platform data",
          "Optimized platform performance, streamlined data pipelines, and collaborated with cross-functional teams to improve internal tool usability",
        ],
        skills: [
          "Front-End Engineering",
          "React",
          "Node.js",
          "Java Spring",
          "MongoDB",
          "Full-Stack Development",
          "Spark/Scala",
          "Kubernetes/K8s",
          "Airflow",
          "UC4",
          "Job Scheduling",
          "ML Engineering",
          "DevOps",
          "Jenkins",
          "n8n",
          "Monitoring",
          "Prometheus",
          "Internal Tools",
          "Automation",
          "Self-Service Platforms",
        ],
      },
      {
        title: "Web Development Engineer – Terawe Corporation",
        time: "July 2018 – Dec 2019 · Bellevue, WA",
        type: "work",
        summary: [
          "Developed internal portals and ML model self-serving tools, combining front-end engineering with backend and DevOps integration.",
        ],
        details: [
          "Designed and implemented a DL/ML model self-serving system to streamline workflows for data science teams",
          "Integrated and customized Kubernetes dashboard features based on internal operational requirements",
          "Designed and developed internal client portals using React and Node.js",
          "Built reusable UI components and improved system usability and performance",
        ],
        skills: [
          "React",
          "Node.js",
          "JavaScript",
          "Kubernetes",
          "ML/DL Tooling",
          "Front-End Engineering",
          "Full-Stack Development",
          "UI Development",
          "System Integration",
        ],
      },
      {
        title:
          "Front-End Software Engineer (Project: Digits) – SitaCorp / T-Mobile",
        time: "Sep 2017 – June 2018 · Bellevue, WA",
        type: "work",
        summary: [
          "Served as a front-end engineer on T-Mobile’s Digits project, developing WebRTC-based real-time communication features for the web platform.",
        ],
        details: [
          "Implemented front-end functionality for WebRTC applications using AngularJS, SASS, HTML, and JSP",
          "Built browser-based calling, conferencing, and messaging features using real-time protocols (WebRTC, WebSocket)",
          "Collaborated with WRC and WRG teams to design, develop, and test Digits UCC user experiences",
          "Worked closely with UX designers, backend engineers, and QA teams to refine UI workflows, improve performance, and resolve issues quickly",
        ],
        skills: [
          "Front-End Engineering",
          "WebRTC",
          "WebSocket",
          "AngularJS",
          "JavaScript",
          "SASS",
          "HTML",
          "JSP",
          "Real-Time Communication",
          "UI Development",
          "Cross-Team Collaboration",
          "Debugging",
        ],
      },
      {
        title: "UI Developer Intern – SitaCorp",
        time: "July 2017 – Sep 2017 · Somerset, NJ",
        type: "work",
        summary: [
          "Contributed to web application and internal tool development using modern JavaScript frameworks under mentorship guidance.",
        ],
        details: [
          "Collaborated with developers using React.js, Angular, and Webpack for UI components and internal tools",
          "Participated in weekly code reviews and daily stand-ups with an intern mentor",
          "Developed an online restaurant reservation application using Angular 4 and Firebase",
          "Implemented responsive UI features and assisted in debugging front-end integration issues",
        ],
        skills: [
          "JavaScript",
          "React.js",
          "Angular",
          "Webpack",
          "Firebase",
          "UI Development",
          "Front-End Engineering",
        ],
      },
      {
        title:
          "System Administrator – China Telecom, Global Network Operation Center",
        time: "Jan 2015 – July 2015 · Shanghai, China",
        type: "work",
        summary: [
          "Supported development and maintenance of a database-driven system used for customs data collection and network resource tracking.",
        ],
        details: [
          "Contributed to backend development using C++ and PHP for resource tracking systems",
          "Maintained ICD infrastructure and ensured high availability of internal applications",
          "Provided technical support for system deployment, upgrades, and troubleshooting",
          "Collaborated with cross-team engineers to improve data accuracy and workflow efficiency",
        ],
        skills: [
          "C++",
          "PHP",
          "MySQL",
          "System Administration",
          "Infrastructure Support",
          "Troubleshooting",
          "Internal Tools",
        ],
      },
      {
        title:
          "Team Leader, Monitoring & Technical Support – China Telecom, Shanghai Transmission Maintenance Center",
        time: "Aug 2006 – Dec 2014 · Shanghai, China",
        type: "work",
        summary: [
          "Led the transmission monitoring team and built internal tools to improve circuit tracking and troubleshooting efficiency.",
        ],
        details: [
          "Managed 24/7 monitoring operations for Shanghai’s backbone transmission network (SDH/WDM/MPLS)",
          "Developed a PHP + MySQL system to track circuit data and improve fault analysis accuracy",
          "Utilized TCP/IP expertise to diagnose anomalies and support customer escalations",
          "Coordinated with field teams and optimized incident response workflows",
        ],
        skills: [
          "Leadership",
          "TCP/IP",
          "PHP",
          "MySQL",
          "Network Monitoring",
          "SDH",
          "Troubleshooting",
        ],
      },
    ],
  },
  {
    title: "Education",
    linePosition: "right",
    dotColor: "#38bdf8",
    dotBorderColor: "#16a34a",
    items: [
      {
        title: "Washington University in St. Louis",
        time: "M.S. in Electrical & Systems Engineering  · Aug 2015 – May 2017 · St. Louis, MO",
        type: "education",
        summary: [
          "Specialized in data mining, machine learning, and large-scale data processing.",

          "Earned university-recognized certificates in Artificial Intelligence and Data Analytics.",
        ],
        details: [
          "Completed advanced coursework in machine learning, probabilistic modeling, statistical learning, and distributed systems.",
          "Gained hands-on experience with big data technologies, including Hadoop-based pipelines and large dataset preprocessing.",
          "Developed machine learning models for predictive analytics and pattern recognition in high-dimensional data.",
          "Awarded graduate-level certificates in AI and Data Analytics for completing interdisciplinary training across engineering and computer science.",
        ],
        skills: [
          "Machine Learning",
          "Data Mining",
          "Big Data Processing",
          "Statistical Modeling",
          "Python",
          "Hadoop",
          "AI & Data Analytics",
        ],
      },
      {
        title: "Shanghai University",
        time: "B.E. in Communication Engineering · Sep 2002 - July 2006 · Shanghai, China",
        type: "education",
        summary: [
          "Graduated within the top 20 students in the department with consecutive academic scholarships.",
          "Built robotics projects integrating embedded systems, sensors, and autonomous control logic.",
        ],
        details: [
          "Ranked in the top 20 of the Communication Engineering major and received scholarships every academic year.",
          "Completed extensive coursework in digital signal processing, communication systems, embedded development, and microcontroller programming.",
          "Designed and built small-scale autonomous robots using sensor arrays, microcontrollers, and C-based control algorithms.",
          "Participated in campus engineering competitions and contributed to lab-based hardware experimentation projects.",
        ],
        skills: [
          "Embedded Systems",
          "Digital Signal Processing",
          "C/C++",
          "Microcontrollers",
          "Robotics",
          "Communication Systems",
        ],
      },
    ],
  },
];

export const HomePage = () => {
  const [chatOpen, setChatOpen] = useState(false);

  return (
    <div className="min-h-screen w-full bg-[#0a0a0a] text-[#e6eef8] flex flex-col items-center">
      {/* Hero Section */}
      <section className="flex flex-col md:flex-row items-center justify-between max-w-[1600px] mx-auto py-32 px-6 w-full">
        <div className="flex-1 space-y-6">
          <h1 className="text-5xl md:text-6xl font-bold leading-tight text-white">
            Weiqing Wang
          </h1>
          <p className="text-xl text-[#a9b0bb]">
            Full‑stack Engineer · AI Automation · 10+ yrs exp
          </p>

          {/* Self-introduction */}
          <p className="text-[#cfd6e4] max-w-2xl mt-4">
            I'm a full-stack engineer with expertise in building large-scale
            data platforms and deploying machine learning systems. I specialize
            in backend development, frontend engineering, workflow orchestration
            with Airflow and Kubernetes, and automating CI/CD pipelines to
            deliver reliable, scalable production systems.
          </p>

          <ContactBar />
          <div className="flex space-x-4 pt-4">
            <Button size="lg">Download Resume</Button>
          </div>
        </div>
        <div className="flex-1 flex justify-center mt-0 md:mt-0">
          <div className="w-64 h-64 md:w-72 md:h-72 rounded-full bg-gradient-to-br from-[#101216] to-[#26292f]" />
        </div>
      </section>

      {/* Timeline Section */}
      <section className="w-full py-4 px-6">
        <Timeline groups={groups} />
      </section>

      <Footer />
      <div
        className="fixed bottom-8"
        style={{ right: "calc((100% - 1600px)/2 - 2rem)" }}
      >
        <ChatDrawer open={chatOpen} onClose={() => setChatOpen(false)} />
        <ChatBubble onClick={() => setChatOpen(true)} />
      </div>
    </div>
  );
};
