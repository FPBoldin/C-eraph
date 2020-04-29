PREFIX     : <http://example.org/> 
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 

DELETE 
{
  ?a foaf:knows ?Var_B .
}
INSERT
{
  ?a foaf:knows ?a .
}
WHERE
{
  { ?a foaf:name "Alan" . }
  { ?a foaf:knows ?Var_B . }
}
