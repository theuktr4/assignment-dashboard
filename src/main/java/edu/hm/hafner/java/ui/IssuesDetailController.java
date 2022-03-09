package edu.hm.hafner.java.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.uc.IssuesTable;

/**
 * Provides detail information for a specific set of {@link Issue issues}.
 *
 * @author Ullrich Hafner
 */
@Controller
public class IssuesDetailController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    /**
     * Creates a new instance of {@link IssuesDetailController}.
     *
     * @param issuesService
     *         service to access the service layer
     */
    @Autowired
    public IssuesDetailController(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * Ajax entry point: returns a table with statistics of the uploaded reports (as JSON object). The returned JSON
     * object is in the expected format for the {@code data} property of a DataTable.
     *
     * @return issues statistics of all uploaded reports
     */
    @RequestMapping(path = "/ajax/issues", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by issues.js
    ResponseEntity<?> getIssues() {
        IssuesTable model = issuesService.createIssuesStatistics();

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }

    /**
     * Ajax entry point: returns the number of issues per category (as JSON object). The returned JSON object is in the
     * expected format for the {@code data} property of a bar chart.
     * <p>
     * Example:
     * <pre>
     *     { "labels" : ["Design","Documentation","Best Practices","Performance","Code Style","Error Prone"],
     *      "datasets" : [
     *          {"data" : [15,3,20,6,53,12]}
     *      ]
     *      }
     * </pre>
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per category
     */
    @RequestMapping(path = "/ajax/categories", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<?> getCategories(@RequestParam("origin") final String origin,
            @RequestParam("reference") final String reference) {
        IssuePropertyDistribution model = issuesService.createDistributionByCategory(origin, reference);

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }

    /**
     * Ajax entry point: returns the number of issues per type (as JSON object). The returned JSON object is in the
     * expected format for the {@code data} property of a bar chart.
     *
     * @param origin
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per type
     */
    @RequestMapping(path = "/ajax/types", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<?> getTypes(@RequestParam("origin") final String origin,
            @RequestParam("reference") final String reference) {
        IssuePropertyDistribution model = issuesService.createDistributionByType(origin, reference);

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }
}
