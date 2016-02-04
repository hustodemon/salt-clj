(ns salt-clj.util
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.walk :as walk]))

(defn nil-safe-name [kw]
  (if (nil? kw)
    nil
    (name kw)))

(defn token->secret [token]
  (:token (first (:return token))))

(defn parse-happy-response
  "Parses a body of a successful HTTP response."
  [res]
  (-> res
    :body
    json/read-str
    walk/keywordize-keys))

(defn build-url [token & url-chunks]
  (reduce 
    #(str %1 "/" %2)
    (cons (:url token) url-chunks)))

(defn salt-get
  "Performs GET on url with given security token and parameters, returns the
  body of the response."
  [token & url-chunks]
  (-> (apply build-url token url-chunks)
    (http/get {:headers {:X-Auth-Token (:password token)}})
    (parse-happy-response)))

(defn salt-post
  "Performs POST on url with given security token and parameters, returns the
  body of the response."
  [token endpoint params]
  (-> 
    (http/post
      (build-url token endpoint)
      {:headers {:X-Auth-Token (:password token)}
       :form-params params})
    parse-happy-response))

