<?php

namespace db_connector\utilities;

class TableCreator
{
    private $number_of_columns;
    private $table;

    public function __construct($number_of_columns)
    {
        $this->number_of_columns = $number_of_columns;
        $this->table = "<table>";
    }

    public function set_super_header(array $columns, array $columns_sizes)
    {
        assert(\count($columns) == \count($columns_sizes) && \array_sum($columns_sizes) == $this->number_of_columns);
        $this->table .= "<tr>";
        for ($i = 0; $i < \count($columns); $i++) {
            $this->table .= "<th colspan='" . $columns_sizes[$i] . "'>" . $columns[$i] . "</th>";
        }
        $this->table .= "</tr>";
    }

    public function set_header(array $columns)
    {
        $this->add_row_to_table($columns, "th");
    }

    public function add_row(array $columns)
    {
        $this->add_row_to_table($columns, "td");
    }

    public function get_table()
    {
        return $this->table . "</table>";
    }

    private function add_row_to_table(array $columns, $type_of_cell)
    {
        assert(\count($columns) == $this->number_of_columns);
        $this->table .= "<tr>";
        foreach ($columns as $value) {
            $this->table .= "<" . $type_of_cell . ">" . $value . "</" . $type_of_cell . ">";
        }
        $this->table .= "</tr>";
    }
}
