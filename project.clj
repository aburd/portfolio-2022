(defproject portfolio-2022 "0.1.0-SNAPSHOT"
  :description "Aaron Burdick's portfolio for 2022"
  :url "http://github.com/aburd/portfolio-2022"
  :main portfolio-2022.core
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [cheshire "5.11.0"]
                 [com.taoensso/tower "3.1.0-beta4"]
                 [hiccup "2.0.0-alpha2"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring/ring-devel "1.9.5"]
                 [ring-refresh "0.1.2"]
                 [metosin/reitit "0.5.18"]]
  :repl-options {:init-ns portfolio-2022.core})
