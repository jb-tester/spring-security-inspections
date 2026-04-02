package com.mytests.spring.springsecurityinspections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * *
 * <p>Created by Irina on 4/2/2026.</p>
 * *
 */
@RestController
@RequestMapping("/")
class HomeController {

    @GetMapping
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("root!");
    }
   /////////       incorrect order tests:   //////////
    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ResponseEntity<String> requestString() {
        return ResponseEntity.ok("home!");
    }
    // this endpoint matches 2 rules, the first will be applied
    @RequestMapping(path = "/home", method = RequestMethod.POST)
    public ResponseEntity<String> requestPost(@RequestBody String body) {
        return ResponseEntity.ok("home post! " + body);
    }

    @RequestMapping(path = "/test1/test1", method = RequestMethod.GET)
    public ResponseEntity<String> requestTest1() {
        return ResponseEntity.ok("test1/test1!");
    }
    // this endpoint matches 2 rules, the first will be applied
    @RequestMapping(path = "/test1/test2", method = RequestMethod.GET)
    public ResponseEntity<String> requestTest2() {
        return ResponseEntity.ok("test1/test2!");
    }

    ////////////    {*var} tests:   /////////////
    // rule /test2/{*var}: this endpoint is shown as NOT matching, but should
    @RequestMapping(path = "/test2/test/test2", method = RequestMethod.GET)
    public ResponseEntity<String> requestTest22() {
        return ResponseEntity.ok("test2/test/test2!");
    }
    // rule /test2/{*var}: this endpoint is shown as matching
    @RequestMapping(path = "/test2/test1", method = RequestMethod.GET)
    public ResponseEntity<String> requestTest21() {
        return ResponseEntity.ok("test2/test1!");
    }
    // /{*var}/test3: this one is detected as matching
    @RequestMapping(path = "/foo/test3", method = RequestMethod.GET)
    public ResponseEntity<String> requestTestFooVarTest3() {
        return ResponseEntity.ok("/foo/test3!");
    }
    // /{*var}/test3: this one is NOT detected as matching
    @RequestMapping(path = "/bar/test/test3", method = RequestMethod.GET)
    public ResponseEntity<String> requestTestBarVar3() {
        return ResponseEntity.ok("/bar/test3!");
    }
}
