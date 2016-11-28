# hibernate-test-case-HHH-10376

Add columns in ORDER BY clause to select list if not already present

When you use "order" + "projection", hibernate doesn t generate the correct query.
 
The obvious workaround is to add the column in the ORDER BY clause as a projection to Projections.
