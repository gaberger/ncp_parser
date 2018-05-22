(ns ncp-parser.frr.core-test
  (:require  [clojure.test :refer :all]
             [clojure.java.io :as io]
             [clojure.spec.alpha :as s]
             [clojure.spec.gen.alpha :as sgen]
             [ncp-parser.frr.parser :refer [create-frr-parser frr]]
             [instaparse.core :as insta :refer :all]
             [ncp-parser.frr.spec :refer :all] 
             [ncp-parser.frr.transform :refer [frr-transform]])) 

; 
(deftest frr-tests
  (testing "Test FRR Parsing"
    (is (not (nil?
               (let [configuration (slurp (io/resource "configs/frr/router1.cfg"))
                     _  (create-frr-parser)]
                  (frr configuration))))))
  (testing "Transformed Result"
    (is (false?
               (let [configuration (slurp (io/resource "configs/frr/router1.cfg"))
                     _  (create-frr-parser)]
                (insta/failure? (frr-transform (frr configuration)))))))
  (testing "Transformed Spec"
    (is (true?
               (let [configuration (slurp (io/resource "configs/frr/router1.cfg"))
                     _  (create-frr-parser)
                     transform-map (frr-transform (frr configuration))]
                  (s/valid? :unq/device transform-map)))))

  (testing "Generating Spec"
    (is (true?
              (let [model (into {} (sgen/sample (s/gen :unq/device)))
                    _  (create-frr-parser)]
                  (s/valid? :unq/device model))))))

